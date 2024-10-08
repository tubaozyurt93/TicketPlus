package com.patika.ticketplusservice.controller;

import com.patika.ticketplusservice.model.Confirmation;
import com.patika.ticketplusservice.service.ConfirmationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/confirmations")
public class ConfirmationController {

    Logger logger = Logger.getLogger(VehicleController.class.getName());

    private final ConfirmationService confirmationService;

    public ConfirmationController(ConfirmationService confirmationService) {
        this.confirmationService = confirmationService;
    }

    @GetMapping
    public ResponseEntity<List<Confirmation>> getAll() {
        List<Confirmation> list = confirmationService.getAll();
        logger.log(Level.INFO, "[ConfirmationController] -- confirmation retrieved successfully. ");
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Confirmation> getConfirmation(@PathVariable Integer id) throws Exception {
        Confirmation confirmation = confirmationService.getConfirmationById(id);
        return ResponseEntity.ok(confirmation);
    }

    @PostMapping
    public ResponseEntity<Confirmation> buy(@RequestBody Confirmation confirmation) throws Exception {
        logger.log(Level.INFO, "[ConfirmationController] -- confirmation created: " + confirmation);
        return new ResponseEntity<>(confirmationService.buy(confirmation), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void deleteConfirmation(@PathVariable("id") Integer id) throws Exception {
        confirmationService.delete(id);
    }
}

