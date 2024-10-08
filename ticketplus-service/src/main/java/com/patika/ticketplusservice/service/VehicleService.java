package com.patika.ticketplusservice.service;

import com.patika.ticketplusservice.converter.VehicleConverter;
import com.patika.ticketplusservice.exception.GeneralException;
import com.patika.ticketplusservice.exception.Message;
import com.patika.ticketplusservice.model.Vehicle;
import com.patika.ticketplusservice.model.enums.ErrorCode;
import com.patika.ticketplusservice.model.enums.VehicleType;
import com.patika.ticketplusservice.repository.VehicleRepository;
import com.patika.ticketplusservice.request.VehicleRequest;
import com.patika.ticketplusservice.response.VehicleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class VehicleService {

    Logger logger = Logger.getLogger(VehicleService.class.getName());


    private final VehicleRepository vehicleRepository;


    private final VehicleConverter converter;

    public VehicleService(VehicleRepository vehicleRepository, VehicleConverter converter) {
        this.vehicleRepository = vehicleRepository;
        this.converter = converter;
    }

    public VehicleResponse create(VehicleRequest vehicleRequest) throws Exception {
        isEmptyVehicleTypeAndCapacity(vehicleRequest.getVehicleType(), vehicleRequest.getCapacity());
        validateCapacity(vehicleRequest.getCapacity());

        Vehicle savedVehicle = vehicleRepository.save(converter.convert(vehicleRequest));
        return converter.convert(savedVehicle);
    }

    public List<VehicleResponse> getVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<VehicleResponse> vehicleResponses = converter.convert(vehicles);
        return vehicleResponses;
    }

    public VehicleResponse getVehicle(Integer id) throws Exception {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new GeneralException(Message.VEHICLE_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        ErrorCode.NOT_FOUND));
        return converter.convert(vehicle);
    }

    public List<VehicleResponse> getVehicleType(String vehicleType) throws Exception {
        List<Vehicle> vehicle = vehicleRepository.findByVehicleType(vehicleType.toUpperCase());
        return converter.convert(vehicle);
    }


    public VehicleResponse updateVehicle(Integer id, VehicleRequest vehicleRequest) throws Exception {
        Vehicle resultVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new GeneralException(Message.VEHICLE_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        ErrorCode.NOT_FOUND));

        isEmptyVehicleTypeAndCapacity(vehicleRequest.getVehicleType(), vehicleRequest.getCapacity());
        validateCapacity(vehicleRequest.getCapacity());

        resultVehicle.setVehicleType(vehicleRequest.getVehicleType());
        resultVehicle.setCapacity(vehicleRequest.getCapacity());
        vehicleRepository.save(resultVehicle);

        return converter.convert(resultVehicle);
    }

    public void deleteVehicle(Integer id) {
        Vehicle deleteVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new GeneralException(Message.VEHICLE_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        ErrorCode.NOT_FOUND));
        vehicleRepository.deleteById(deleteVehicle.getId());
    }

    private void isEmptyVehicleTypeAndCapacity(VehicleType vehicleType, Integer capacity) throws Exception {
        if (vehicleType == null && capacity == null) {
            logger.log(Level.WARNING, "Vehicle type and capacity cannot be empty.");
            throw new GeneralException(Message.VEHICLE_TYPE_AND_CAPACITY_NOT_NULL,
                    HttpStatus.BAD_REQUEST,
                    ErrorCode.NOT_NULL);
        }
    }

    private void validateCapacity(Integer capacity) throws Exception {
        if (capacity < 0) {
            logger.log(Level.WARNING, "[VehicleService] -- Invalid capacity provided: {0}. ", capacity);
            throw new GeneralException(Message.CAPACITY_LESS_THAN_ZERO,
                    HttpStatus.BAD_REQUEST,
                    ErrorCode.BAD_REQUEST);
        }
    }
}