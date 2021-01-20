package femi.core.utils;

import org.jbehave.core.model.ExamplesTable;

import java.util.Map;

public class ExamplesTableHelper {

    public static Map<String, String> getMapFromExamplesTableByRow(ExamplesTable table, int index){
        return table.getRow(index);
    }

}
