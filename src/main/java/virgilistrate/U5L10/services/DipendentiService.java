package virgilistrate.U5L10.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import virgilistrate.U5L10.entities.Dipendente;
import virgilistrate.U5L10.exceptions.BadRequestException;
import virgilistrate.U5L10.exceptions.NotFoundException;
import virgilistrate.U5L10.payloads.DipendenteDTO;
import virgilistrate.U5L10.payloads.DipendentePayload;
import virgilistrate.U5L10.repositories.DipendentiRepository;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class DipendentiService {

    private final DipendentiRepository dipendentiRepository;
    private final Cloudinary cloudinaryUploader;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DipendentiService(DipendentiRepository dipendentiRepository,
                             Cloudinary cloudinaryUploader,
                             PasswordEncoder passwordEncoder) {
        this.dipendentiRepository = dipendentiRepository;
        this.cloudinaryUploader = cloudinaryUploader;
        this.passwordEncoder = passwordEncoder;
    }

    public Dipendente save(DipendenteDTO payload) {

        this.dipendentiRepository.findByEmail(payload.email()).ifPresent(d -> {
            throw new BadRequestException("L'email " + d.getEmail() + " è già in uso!");
        });

        this.dipendentiRepository.findByUsername(payload.username()).ifPresent(d -> {
            throw new BadRequestException("Lo username " + d.getUsername() + " è già in uso!");
        });


        String hashedPassword = passwordEncoder.encode(payload.password());

        Dipendente newDipendente = new Dipendente(
                payload.username(),
                payload.name(),
                payload.surname(),
                payload.email(),
                hashedPassword
        );

        newDipendente.setAvatarURL("https://ui-avatars.com/api?name=" + payload.name() + "+" + payload.surname());

        Dipendente saved = this.dipendentiRepository.save(newDipendente);

        log.info("Il dipendente con id " + saved.getId() + " è stato salvato correttamente!");
        return saved;
    }

    public Page<Dipendente> findAll(int page, int size, String orderBy, String sortCriteria) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(
                page,
                size,
                sortCriteria.equalsIgnoreCase("desc") ? Sort.by(orderBy).descending() : Sort.by(orderBy)
        );

        return this.dipendentiRepository.findAll(pageable);
    }

    public Dipendente findById(UUID id) {
        return this.dipendentiRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Dipendente findByIdAndUpdate(UUID id, DipendentePayload payload) {
        Dipendente found = this.findById(id);

        if (!found.getEmail().equals(payload.getEmail())) {
            this.dipendentiRepository.findByEmail(payload.getEmail()).ifPresent(d -> {
                throw new BadRequestException("L'email " + d.getEmail() + " è già in uso!");
            });
        }

        if (!found.getUsername().equals(payload.getUsername())) {
            this.dipendentiRepository.findByUsername(payload.getUsername()).ifPresent(d -> {
                throw new BadRequestException("Lo username " + d.getUsername() + " è già in uso!");
            });
        }

        found.setUsername(payload.getUsername());
        found.setName(payload.getName());
        found.setSurname(payload.getSurname());
        found.setEmail(payload.getEmail());


        found.setAvatarURL("https://ui-avatars.com/api?name=" + payload.getName() + "+" + payload.getSurname());

        Dipendente modified = this.dipendentiRepository.save(found);

        log.info("Il dipendente con id " + modified.getId() + " è stato modificato correttamente!");
        return modified;
    }

    public void findByIdAndDelete(UUID id) {
        Dipendente found = this.findById(id);
        this.dipendentiRepository.delete(found);
    }

    public String uploadAvatar(MultipartFile file) {
        try {
            Map result = cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return (String) result.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Dipendente uploadAvatarAndUpdate(UUID dipendenteId, MultipartFile file) {
        Dipendente found = this.findById(dipendenteId);

        String url = this.uploadAvatar(file);

        found.setAvatarURL(url);
        Dipendente modified = this.dipendentiRepository.save(found);

        log.info("L'avatar del dipendente con id " + modified.getId() + " è stato aggiornato correttamente!");
        return modified;
    }
}
