package com.find.service;

import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.util.List;

public interface PlotService {
    String getAccGraphById(String cliId, List<Float> accList) throws PythonExecutionException, IOException;

}
