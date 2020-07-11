package com.mod.rest.service;

import java.io.File;
import java.util.List;

public interface PDFServiceI {

    public File generate(List<?> objects, String filename, String tagName) throws Exception;
}
