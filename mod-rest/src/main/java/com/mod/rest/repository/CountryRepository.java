package com.mod.rest.repository;

import com.mod.rest.model.Country;
import java.util.List;
import java.util.Optional;

public interface CountryRepository extends GenericRepository<Country, Long> {
//    public Optional<Country> findOneByCountryLogoImageAndId(String countryLogoImage, long id);
        Optional<Country> findById(long id);


}
