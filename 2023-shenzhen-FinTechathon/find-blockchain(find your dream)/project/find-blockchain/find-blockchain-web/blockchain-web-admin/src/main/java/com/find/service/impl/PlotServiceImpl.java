package com.find.service.impl;




import com.find.util.DataPlot;
import com.find.service.PlotService;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PlotServiceImpl implements PlotService {

    @Override
    public String getAccGraphById(String cliId, List<Float> accList) throws PythonExecutionException, IOException {
        String cliIdNum = cliId.substring(cliId.length()-1);

//        System.out.println("cliIdNum is : "+cliIdNum);

        return DataPlot.getAccGraph(accList,cliIdNum);
    }
}
