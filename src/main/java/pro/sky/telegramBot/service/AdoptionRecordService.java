package pro.sky.telegramBot.service;

import pro.sky.telegramBot.model.Adoption.Report;

import java.time.LocalDate;

public interface AdoptionRecordService {
    Report getCurrentReport(Long id, LocalDate date);

    void addNewReportToAdoptionRecord(Report newReport, int reportResult, Long chatId);

    void checkNewAdopter();

    void informAdopterAboutStartReporting();

    void informAdopterAboutEndReporting();
}
