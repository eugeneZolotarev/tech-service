package cdpoitmo.main_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "token")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "revoked", nullable = false)
    private boolean revoked;

    @Column(name = "expired", nullable = false)
    private boolean expired;

    @Column(name = "refreshToken", nullable = false, unique = true)
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ApplicationUser applicationUser;
}
