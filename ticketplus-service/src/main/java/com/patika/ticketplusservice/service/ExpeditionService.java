package com.patika.ticketplusservice.service;


import com.patika.ticketplusservice.converter.ExpeditionConverter;
import com.patika.ticketplusservice.exception.GeneralException;
import com.patika.ticketplusservice.exception.Message;
import com.patika.ticketplusservice.model.Expedition;
import com.patika.ticketplusservice.model.enums.ErrorCode;
import com.patika.ticketplusservice.repository.ExpeditionRepository;
import com.patika.ticketplusservice.request.ExpeditionRequest;
import com.patika.ticketplusservice.response.ExpeditionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ExpeditionService {

    Logger logger = Logger.getLogger(ExpeditionService.class.getName());

    private final ExpeditionRepository expeditionRepository;

    private final ExpeditionConverter converter;

    public ExpeditionService(ExpeditionRepository expeditionRepository,
                             ExpeditionConverter converter) {
        this.expeditionRepository = expeditionRepository;
        this.converter = converter;
    }

    public ExpeditionResponse create(ExpeditionRequest expeditionRequest) {
        Expedition savedExpedition = converter.convert(expeditionRequest);
        savedExpedition.setNumberOfTicketsRemaining(savedExpedition.getVehicle().getCapacity());
        expeditionRepository.save(savedExpedition);
        return converter.convert(savedExpedition);
    }

    public List<ExpeditionResponse> getAll() {
        List<Expedition> expeditions = expeditionRepository.findAll();
        List<ExpeditionResponse> expeditionResponses = converter.convert(expeditions);
        return expeditionResponses;
    }


    //@Cacheable(value = "totalTicketSales", key = "#id")
    public Integer getTotalTicketSales(Integer id) throws Exception {
        Integer totalTicketSales = (getByExpeditionId(id).getVehicle().getCapacity())
                - (getByExpeditionId(id).getNumberOfTicketsRemaining());

        logger.log(Level.INFO,
                "[ExpeditionService] -- The total ticket sales for expedition id {0} are {1}"
                , new Object[]{id, totalTicketSales});
        return totalTicketSales;
    }


    public ExpeditionResponse getByExpeditionId(Integer id) throws Exception {
        Expedition expedition = expeditionRepository.findById(id)
                .orElseThrow(() -> new GeneralException(Message.NO_EXPEDITION_FOUND,
                        HttpStatus.NOT_FOUND,
                        ErrorCode.NOT_FOUND));
        logger.log(Level.INFO, "[ExpeditionService] -- The expedition was brought by id: {0}. ", id);
        return converter.convert(expedition);
    }


    public Expedition getById(Integer id) throws Exception {
        Expedition expedition = expeditionRepository.findById(id).orElseThrow();
        return expedition;
    }

    public List<ExpeditionResponse> getByExpeditionLocation(String fromCity, String toCity) {
        List<Expedition> found = expeditionRepository.findByFromCityIgnoreCaseAndToCityIgnoreCase(fromCity, toCity);

        logger.log(Level.INFO,
                "[ExpeditionService] -- The expeditions from {0} to {1} have been retrieved",
                new Object[]{fromCity, toCity});
        return converter.convert(found);
    }

    public List<ExpeditionResponse> getByExpeditionVehicleType(String vehicleType) throws Exception {
        List<ExpeditionResponse> expeditions = getAll();
        List<ExpeditionResponse> getVehicleType = expeditions.stream()
                .filter(expeditionResponse -> expeditionResponse
                        .getVehicle()
                        .getVehicleType().toString().equals(vehicleType)).collect(Collectors.toList());

        return getVehicleType;
    }

    public List<Expedition> getByExpeditionDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate formatDate = LocalDate.parse(date, formatter);
        return expeditionRepository.findAll().stream()
                .filter(expedition -> expedition.getDepartureDate().equals(formatDate))
                .collect(Collectors.toList());
    }

    public ExpeditionResponse update(Integer id, ExpeditionRequest expeditionRequest) throws Exception {
        Expedition resultExpedition = expeditionRepository.findById(id)
                .orElseThrow(() -> new GeneralException(Message.NO_EXPEDITION_FOUND,
                        HttpStatus.NOT_FOUND,
                        ErrorCode.NOT_FOUND));
        resultExpedition.setTo(expeditionRequest.getTo());
        resultExpedition.setFrom(expeditionRequest.getFrom());
        resultExpedition.setCompany(expeditionRequest.getCompany());
        resultExpedition.setDepartureDate(expeditionRequest.getDepartureDate());
        Expedition savedExpedition = expeditionRepository.save(converter.convert(expeditionRequest));
        return converter.convert(savedExpedition);

    }


    public void delete(Integer id) {
        Expedition deleted = expeditionRepository.findById(id)
                .orElseThrow(() -> new GeneralException(Message.NO_EXPEDITION_FOUND,
                        HttpStatus.NOT_FOUND,
                        ErrorCode.NOT_FOUND));
        expeditionRepository.deleteById(deleted.getId());
    }

}
