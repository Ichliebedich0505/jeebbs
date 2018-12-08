package edu.scut.jeebbs.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import edu.scut.jeebbs.domain.Stock;

import java.io.IOException;

public class StockDeserializer extends JsonDeserializer<Stock> {

    @Override
    public Stock deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        Stock stock = new Stock();

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        stock.setCur(node.get(1).floatValue());
        stock.setId(node.get(0).intValue());
        return stock;
    }
}
