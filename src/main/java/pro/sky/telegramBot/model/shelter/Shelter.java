package pro.sky.telegramBot.model.shelter;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import pro.sky.telegramBot.enums.ShelterTypes;
import pro.sky.telegramBot.model.pet.Cat;
import pro.sky.telegramBot.model.pet.Dog;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "приют")
@RequiredArgsConstructor
@Data
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "название")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "тип_приюта")
    private ShelterTypes type;

    @OneToOne
    @JoinColumn(name = "информация_о_приюте_id")
    private ShelterInfo shelterInfo;

    @OneToMany(mappedBy = "shelter")
    private Collection<Dog> dogs;

    @OneToMany(mappedBy = "shelter")
    private Collection<Cat> cats;
}
