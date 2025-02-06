public class WorldState {
    public static final int DAY_LENGTH = 200;
    public static final int NIGHT_LENGTH = 100;
    private WeatherState weatherState;
    private TimeOfDay timeOfDay;

    public WeatherState getWeatherState() {
        return weatherState;
    }

    public void setWeatherState(WeatherState weatherState) {
        this.weatherState = weatherState;
    }

    public TimeOfDay getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(TimeOfDay timeOfDay) {
        this.timeOfDay = timeOfDay;
    }
}
