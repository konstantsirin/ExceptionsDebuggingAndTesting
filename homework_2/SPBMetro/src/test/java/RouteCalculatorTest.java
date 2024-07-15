import core.Line;
import core.Station;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class RouteCalculatorTest {
    private StationIndex stationIndex;

    @Test
    @DisplayName("Тест пути если указана одна и та же станция")
    void testGetShortestFromEqualsTo() {
        RouteCalculator calculator = getRouteCalculator();
        Station stationFrom = stationIndex.getStation("Лесная");
        Station stationTo = stationIndex.getStation("Лесная");

        List<Station> route = calculator.getShortestRoute(stationFrom, stationTo);
        assertEquals(List.of(stationFrom), route);
    }

    @Test
    @DisplayName("Тест пути по одной линии")
    void testGetShortestRouteOnTheLine() {
        RouteCalculator calculator = getRouteCalculator();
        Station stationFrom = stationIndex.getStation("Лесная");
        Station stationTo = stationIndex.getStation("Чернышевская");

        List<Station> route = calculator.getShortestRoute(stationFrom, stationTo);
        List<Station> expected = List.of(
                stationIndex.getStation("Лесная"),
                stationIndex.getStation("Выборгская"),
                stationIndex.getStation("Площадь Ленина"),
                stationIndex.getStation("Чернышевская")
        );
        assertEquals(expected, route);
    }

    @Test
    @DisplayName("Тест пути с одной пересадкой")
    void testGetShortestRouteWithOneConnection() {
        RouteCalculator calculator = getRouteCalculator();
        Station stationFrom = stationIndex.getStation("Гостиный двор");
        Station stationTo = stationIndex.getStation("Лесная");

        List<Station> route = calculator.getShortestRoute(stationFrom, stationTo);
        List<Station> expected = List.of(
                stationIndex.getStation("Гостиный двор"),
                stationIndex.getStation("Маяковская"),
                stationIndex.getStation("Площадь Восстания"),
                stationIndex.getStation("Чернышевская"),
                stationIndex.getStation("Площадь Ленина"),
                stationIndex.getStation("Выборгская"),
                stationIndex.getStation("Лесная")
        );
        assertEquals(expected, route);
    }

    @Test
    @DisplayName("Тест пути с двумя пересадками")
    void testGetShortestRouteWithTwoConnections() {
        RouteCalculator calculator = getRouteCalculator();
        Station stationFrom = stationIndex.getStation("Горьковская");
        Station stationTo = stationIndex.getStation("Лесная");

        List<Station> route = calculator.getShortestRoute(stationFrom, stationTo);
        List<Station> expected = List.of(
                stationIndex.getStation("Горьковская"),
                stationIndex.getStation("Невский проспект"),
                stationIndex.getStation("Гостиный двор"),
                stationIndex.getStation("Маяковская"),
                stationIndex.getStation("Площадь Восстания"),
                stationIndex.getStation("Чернышевская"),
                stationIndex.getStation("Площадь Ленина"),
                stationIndex.getStation("Выборгская"),
                stationIndex.getStation("Лесная")
        );
        assertEquals(expected, route);
    }

    @Test
    @DisplayName("Тест пути с двумя пересадками в обратном порядке")
    void testGetShortestRouteWithTwoConnectionsReverse() {
        RouteCalculator calculator = getRouteCalculator();
        Station stationFrom = stationIndex.getStation("Лесная");
        Station stationTo = stationIndex.getStation("Горьковская");

        List<Station> route = calculator.getShortestRoute(stationFrom, stationTo);
        List<Station> expected = List.of(
                stationIndex.getStation("Лесная"),
                stationIndex.getStation("Выборгская"),
                stationIndex.getStation("Площадь Ленина"),
                stationIndex.getStation("Чернышевская"),
                stationIndex.getStation("Площадь Восстания"),
                stationIndex.getStation("Маяковская"),
                stationIndex.getStation("Гостиный двор"),
                stationIndex.getStation("Невский проспект"),
                stationIndex.getStation("Горьковская")
        );
        assertEquals(expected, route);
    }

    @Test
    @DisplayName("Тест времени пути между станциями")
    void testCalculateDuration() {
        RouteCalculator calculator = getRouteCalculator();
        Station stationFrom = stationIndex.getStation("Гостиный двор");
        Station stationTo = stationIndex.getStation("Лесная");
        List<Station> route = calculator.getShortestRoute(stationFrom, stationTo);
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 16.0;
        assertEquals(expected, actual);
    }



    private RouteCalculator getRouteCalculator() {
        createStationIndex();
        return new RouteCalculator(stationIndex);
    }

    private void createStationIndex() {
        stationIndex = new StationIndex();
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonData = (JSONObject) parser.parse(getJsonFile());

            JSONArray linesArray = (JSONArray) jsonData.get("lines");
            parseLines(linesArray);

            JSONObject stationsObject = (JSONObject) jsonData.get("stations");
            parseStations(stationsObject);

            JSONArray connectionsArray = (JSONArray) jsonData.get("connections");
            parseConnections(connectionsArray);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void parseConnections(JSONArray connectionsArray) {
        connectionsArray.forEach(connectionObject ->
        {
            JSONArray connection = (JSONArray) connectionObject;
            List<Station> connectionStations = new ArrayList<>();
            connection.forEach(item ->
            {
                JSONObject itemObject = (JSONObject) item;
                int lineNumber = ((Long) itemObject.get("line")).intValue();
                String stationName = (String) itemObject.get("station");

                Station station = stationIndex.getStation(stationName, lineNumber);
                if (station == null) {
                    throw new IllegalArgumentException("core.Station " +
                            stationName + " on line " + lineNumber + " not found");
                }
                connectionStations.add(station);
            });
            stationIndex.addConnection(connectionStations);
        });
    }

    private void parseStations(JSONObject stationsObject) {
        stationsObject.keySet().forEach(lineNumberObject ->
        {
            int lineNumber = Integer.parseInt((String) lineNumberObject);
            Line line = stationIndex.getLine(lineNumber);
            JSONArray stationsArray = (JSONArray) stationsObject.get(lineNumberObject);
            stationsArray.forEach(stationObject ->
            {
                Station station = new Station((String) stationObject, line);
                stationIndex.addStation(station);
                line.addStation(station);
            });
        });
    }

    private void parseLines(JSONArray linesArray) {
        linesArray.forEach(lineObject -> {
            JSONObject lineJsonObject = (JSONObject) lineObject;
            Line line = new Line(
                    ((Long) lineJsonObject.get("number")).intValue(),
                    (String) lineJsonObject.get("name")
            );
            stationIndex.addLine(line);
        });
    }

    private String getJsonFile() {
        StringBuilder builder = new StringBuilder();
        try {
            String DATA_FILE = "src/main/resources/map.json";
            List<String> lines = Files.readAllLines(Paths.get(DATA_FILE));
            lines.forEach(builder::append);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return builder.toString();
    }
}