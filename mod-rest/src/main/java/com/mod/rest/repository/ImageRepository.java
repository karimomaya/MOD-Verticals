package com.mod.rest.repository;

import com.mod.rest.model.Image;

import java.util.List;
import java.util.Optional;

/**
 * Created by karim.omaya on 12/10/2019.
 */
public interface ImageRepository extends GenericRepository<Image, Long> {
    public List<Image> findOneByTypeAndParentItemId(String type, String parentItemId);
}
