package pro.sky.telegramBot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.model.volunteer.Volunteer;
import pro.sky.telegramBot.repository.VolunteerRepository;
import pro.sky.telegramBot.service.VolunteerService;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VolunteerServiceImpl implements VolunteerService {

    private final VolunteerRepository volunteerRepository;

    @Override
    public Volunteer create(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    @Override
    public Volunteer update(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    @Override
    public List<Volunteer> findVolunteerByChayId(Long chatId) {
        return volunteerRepository.findByChatId(chatId);
    }
}