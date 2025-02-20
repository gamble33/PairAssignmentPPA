import java.util.Random;

public class WorldState {
    public static final int DAY_LENGTH = 200;
    public static final int NIGHT_LENGTH = 100;

    private static final Random rand = Randomizer.getRandom();
    private WeatherState weatherState;
    private TimeOfDay timeOfDay;
    private int time;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void incrementTime() {
        time++;
    }

    public WeatherState getWeatherState() {
        return weatherState;
    }

    public void setWeatherState(WeatherState weatherState) {
        this.weatherState = weatherState;
    }

    public void setRandomWeather() {
        double randomNumber = rand.nextDouble();
        if (randomNumber < 0.70d) setWeatherState(WeatherState.Sunny);
        else if (randomNumber < 0.85d) setWeatherState(WeatherState.Thunderstorm);
        else setWeatherState(WeatherState.Heatwave);
    }

    public TimeOfDay getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(TimeOfDay timeOfDay) {
        this.timeOfDay = timeOfDay;
    }
}
