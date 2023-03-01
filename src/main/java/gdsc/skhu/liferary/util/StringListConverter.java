package gdsc.skhu.liferary.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if(attribute == null) {
            return "";
        }
        return String.join(SPLIT_CHAR, attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String entity) {
        if (entity.isBlank()) {
            return null;
        } else {
            return Arrays.asList(entity.split(SPLIT_CHAR));
        }
    }
}
