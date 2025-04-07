package org.kurgu.moviemanagement.Controllers;

// import jakarta.validation.Valid; // Kaldırıldı veya yorum satırı yapıldı
import org.kurgu.moviemanagement.DTOs.ClassificationRequestDTO;
import org.kurgu.moviemanagement.DTOs.ClassificationResponseDTO;
import org.kurgu.moviemanagement.Models.Category;
import org.kurgu.moviemanagement.Models.Classification;
import org.kurgu.moviemanagement.Models.Movie;
import org.kurgu.moviemanagement.Repositories.CategoryRepository;
import org.kurgu.moviemanagement.Repositories.ClassificationRepository;
import org.kurgu.moviemanagement.Repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Loglama için importlar
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/classifications")
public class ClassificationController {

    private static final Logger log = LoggerFactory.getLogger(ClassificationController.class);

    private final ClassificationRepository classificationRepository;
    private final MovieRepository movieRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ClassificationController(ClassificationRepository classificationRepository,
                                    MovieRepository movieRepository,
                                    CategoryRepository categoryRepository) {
        this.classificationRepository = classificationRepository;
        this.movieRepository = movieRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAllClassifications() {
        log.info("==> GET /api/v1/classifications çağrıldı.");
        try {
            List<Classification> classifications = classificationRepository.findByIsdeletedFalse();
            log.info("Aktif classification sayısı: {}", classifications.size());

            List<ClassificationResponseDTO> responseDTOs = new ArrayList<>();
            for (Classification c : classifications) {
                log.debug("İşlenen classification ID: {}", c.getClassificationId());
                Optional<Movie> movieOpt = movieRepository.findById(c.getMovieId());
                Optional<Category> categoryOpt = categoryRepository.findById(c.getCategoryId());

                if (movieOpt.isPresent() && categoryOpt.isPresent()) {
                    log.debug("İlişkili Movie ID {} ve Category ID {} bulundu.", c.getMovieId(), c.getCategoryId());
                    try {
                        ClassificationResponseDTO dto = ClassificationResponseDTO.fromEntities(c, movieOpt.get(), categoryOpt.get());
                        responseDTOs.add(dto);
                        log.debug("DTO oluşturuldu: {}", dto);
                    } catch (Exception e) {
                        log.error("DTO oluşturulurken hata oluştu (Classification ID: {}): {}", c.getClassificationId(), e.getMessage(), e);
                    }
                } else {
                    log.warn("!!! Classification ID {} için eksik ilişki! Movie bulundu: {}, Category bulundu: {}",
                            c.getClassificationId(), movieOpt.isPresent(), categoryOpt.isPresent());
                }
            }
            log.info("<== Toplam {} DTO oluşturuldu. Yanıt gönderiliyor.", responseDTOs.size());
            return ResponseEntity.ok(responseDTOs);

        } catch (Exception e) {
            log.error("!!! getAllClassifications sırasında genel hata: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sınıflandırmalar alınırken sunucu hatası oluştu.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClassificationById(@PathVariable int id) {
        log.info("==> GET /api/v1/classifications/{} çağrıldı.", id);
        try {
            Optional<Classification> classificationOpt = classificationRepository.findActiveById(id);

            if (classificationOpt.isPresent()) {
                Classification c = classificationOpt.get();
                log.info("Classification bulundu: ID {}", c.getClassificationId());
                Optional<Movie> movieOpt = movieRepository.findById(c.getMovieId());
                Optional<Category> categoryOpt = categoryRepository.findById(c.getCategoryId());

                if (movieOpt.isPresent() && categoryOpt.isPresent()) {
                    log.debug("İlişkili Movie ID {} ve Category ID {} bulundu.", c.getMovieId(), c.getCategoryId());
                    ClassificationResponseDTO dto = ClassificationResponseDTO.fromEntities(c, movieOpt.get(), categoryOpt.get());
                    log.info("<== DTO oluşturuldu ve yanıt gönderiliyor: {}", dto);
                    return ResponseEntity.ok(dto);
                } else {
                    log.error("!!! Classification ID {} için eksik ilişki! Movie bulundu: {}, Category bulundu: {}",
                            c.getClassificationId(), movieOpt.isPresent(), categoryOpt.isPresent());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("İlişkili veriler bulunamadığı için sınıflandırma bilgisi alınamıyor.");
                }
            } else {
                log.warn("!!! Aktif Classification ID {} bulunamadı.", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("!!! getClassificationById sırasında genel hata (ID: {}): {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sınıflandırma alınırken sunucu hatası oluştu.");
        }
    }

    @PostMapping
    public ResponseEntity<?> createClassification(@RequestBody ClassificationRequestDTO requestDTO) {
        log.info("==> POST /api/v1/classifications çağrıldı. RequestBody: {}", requestDTO);
        try {
            if (requestDTO.getMovieId() <= 0 || requestDTO.getCategoryId() <= 0) {
                log.warn("!!! Geçersiz (0 veya negatif) Film veya Kategori ID'si geldi.");
                return ResponseEntity.badRequest().body("Film ID ve Kategori ID pozitif olmalıdır.");
            }

            Optional<Movie> movieOpt = movieRepository.findById(requestDTO.getMovieId());
            Optional<Category> categoryOpt = categoryRepository.findById(requestDTO.getCategoryId());

            if (movieOpt.isEmpty() || categoryOpt.isEmpty()) {
                log.warn("!!! Geçersiz Film ({}) veya Kategori ({}) ID'si.", requestDTO.getMovieId(), requestDTO.getCategoryId());
                return ResponseEntity.badRequest().body("Geçersiz Film veya Kategori ID'si.");
            }
            log.info("Film ve Kategori bulundu.");

            Optional<Classification> existing = classificationRepository.findByMovieIdAndCategoryIdAndIsdeletedFalse(
                    requestDTO.getMovieId(), requestDTO.getCategoryId());

            if (existing.isPresent()) {
                log.warn("!!! Çakışma: Film ID {} zaten Kategori ID {} ile eşleşmiş (Aktif Classification ID: {}).",
                        requestDTO.getMovieId(), requestDTO.getCategoryId(), existing.get().getClassificationId());
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Bu film zaten bu kategoriye atanmış.");
            }
            log.info("Mevcut aktif eşleşme bulunamadı, yeni kayıt oluşturulacak.");

            Classification newClassification = new Classification();
            newClassification.setMovieId(requestDTO.getMovieId());
            newClassification.setCategoryId(requestDTO.getCategoryId());
            newClassification.setDate(LocalDate.now());
            newClassification.setIsdeleted(false);

            Classification savedClassification = classificationRepository.save(newClassification);
            log.info("Yeni classification kaydedildi. ID: {}", savedClassification.getClassificationId());

            ClassificationResponseDTO response = ClassificationResponseDTO.fromEntities(
                    savedClassification, movieOpt.get(), categoryOpt.get());
            log.info("<== Yanıt DTO'su oluşturuldu: {}", response);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            log.error("!!! createClassification sırasında genel hata: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sınıflandırma oluşturulurken sunucu hatası oluştu.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClassification(@PathVariable int id, @RequestBody ClassificationRequestDTO requestDTO) { // @Valid kaldırıldı
        log.info("==> PUT /api/v1/classifications/{} çağrıldı. RequestBody: {}", id, requestDTO);
        try{
            if (requestDTO.getMovieId() <= 0 || requestDTO.getCategoryId() <= 0) {
                log.warn("!!! Geçersiz (0 veya negatif) Film veya Kategori ID'si geldi.");
                return ResponseEntity.badRequest().body("Film ID ve Kategori ID pozitif olmalıdır.");
            }

            Optional<Classification> classificationOpt = classificationRepository.findActiveById(id);

            if (classificationOpt.isEmpty()) {
                log.warn("!!! Güncellenecek Aktif Classification ID {} bulunamadı.", id);
                return ResponseEntity.notFound().build();
            }

            Optional<Movie> movieOpt = movieRepository.findById(requestDTO.getMovieId());
            Optional<Category> categoryOpt = categoryRepository.findById(requestDTO.getCategoryId());
            if (movieOpt.isEmpty() || categoryOpt.isEmpty()) {
                log.warn("!!! Geçersiz Film ({}) veya Kategori ({}) ID'si.", requestDTO.getMovieId(), requestDTO.getCategoryId());
                return ResponseEntity.badRequest().body("Geçersiz Film veya Kategori ID'si.");
            }

            Optional<Classification> existingConflict = classificationRepository.findByMovieIdAndCategoryIdAndIsdeletedFalse(
                    requestDTO.getMovieId(), requestDTO.getCategoryId());
            if(existingConflict.isPresent() && existingConflict.get().getClassificationId() != id) {
                log.warn("!!! Çakışma: Film ID {} zaten Kategori ID {} ile eşleşmiş (Başka bir aktif Classification ID: {}).",
                        requestDTO.getMovieId(), requestDTO.getCategoryId(), existingConflict.get().getClassificationId());
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Bu film zaten bu kategoriye atanmış (başka bir kayıt).");
            }

            Classification existingClassification = classificationOpt.get();
            existingClassification.setMovieId(requestDTO.getMovieId());
            existingClassification.setCategoryId(requestDTO.getCategoryId());

            Classification updatedClassification = classificationRepository.save(existingClassification);
            log.info("Classification güncellendi. ID: {}", updatedClassification.getClassificationId());

            ClassificationResponseDTO response = ClassificationResponseDTO.fromEntities(
                    updatedClassification, movieOpt.get(), categoryOpt.get());
            log.info("<== Yanıt DTO'su oluşturuldu: {}", response);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("!!! updateClassification sırasında genel hata (ID: {}): {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sınıflandırma güncellenirken sunucu hatası oluştu.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassification(@PathVariable int id) {
        log.info("==> DELETE /api/v1/classifications/{} çağrıldı.", id);
        try {
            Optional<Classification> optionalClassification = classificationRepository.findById(id);

            if (optionalClassification.isPresent()) {
                Classification classification = optionalClassification.get();
                if (classification.isIsdeleted()) {
                    log.info("Classification ID {} zaten silinmiş durumda.", id);
                    return ResponseEntity.noContent().build();
                }
                classification.setIsdeleted(true);
                classificationRepository.save(classification);
                log.info("Classification ID {} silindi (soft delete).", id);
                return ResponseEntity.noContent().build();
            } else {
                log.warn("!!! Silinecek Classification ID {} bulunamadı.", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("!!! deleteClassification sırasında genel hata (ID: {}): {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}