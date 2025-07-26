package com.relatia.customer_service.organisation;

import com.relatia.customer_service.model.OrganisationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.relatia.customer_service.constants.GlobalConstants.ORGANISATION_API;


@RequestMapping(path = ORGANISATION_API)
@RequiredArgsConstructor
@RestController
class OrganisationController {

    private final OrganisationInfo organisationInfo;

    @GetMapping
    ResponseEntity<OrganisationInfo>  info(){
        return ResponseEntity.ok(organisationInfo);
    }
}
