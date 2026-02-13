package virgilistrate.U5L10.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "viaggi")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Viaggio {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    private String destinazione;

    private LocalDate data;

    @Enumerated(EnumType.STRING)
    private ViaggioStatus status;


    @ManyToOne
    @JoinColumn(name = "dipendente_id")
    private Dipendente dipendente;

    public Viaggio(String destinazione, LocalDate data, ViaggioStatus status) {
        this.destinazione = destinazione;
        this.data = data;
        this.status = status;
    }
}
