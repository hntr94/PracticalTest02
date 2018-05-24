package ro.pub.cs.systems.eim.practicaltest02.model;

public class TimerInformation {

//    private String temperature;
//    private String windSpeed;
//    private String condition;
//    private String pressure;
//    private String humidity;

    private String hour;
    private String minute;

    public TimerInformation() {
//        this.temperature = null;
//        this.windSpeed = null;
//        this.condition = null;
//        this.pressure = null;
//        this.humidity = null;
        this.hour = null;
        this.minute = null;
    }

//    public TimerInformation(String temperature, String windSpeed, String condition, String pressure, String humidity) {
//        this.temperature = temperature;
//        this.windSpeed = windSpeed;
//        this.condition = condition;
//        this.pressure = pressure;
//        this.humidity = humidity;
//    }

    public TimerInformation(String hour, String minute) {
        this.hour= hour;
        this.minute = minute;
    }

//    public String getTemperature() {
//        return temperature;
//    }
//
//    public void setTemperature(String temperature) {
//        this.temperature = temperature;
//    }
//
//    public String getWindSpeed() {
//        return windSpeed;
//    }
//
//    public void setWindSpeed(String windSpeed) {
//        this.windSpeed = windSpeed;
//    }
//
//    public String getCondition() {
//        return condition;
//    }
//
//    public void setCondition(String condition) {
//        this.condition = condition;
//    }
//
//    public String getPressure() {
//        return pressure;
//    }
//
//    public void setPressure(String pressure) {
//        this.pressure = pressure;
//    }
//
//    public String getHumidity() {
//        return humidity;
//    }
//
//    public void setHumidity(String humidity) {
//        this.humidity = humidity;
//    }


    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    @Override
//    public String toString() {
//        return "TimerInformation{" +
//                "temperature='" + temperature + '\'' +
//                ", windSpeed='" + windSpeed + '\'' +
//                ", condition='" + condition + '\'' +
//                ", pressure='" + pressure + '\'' +
//                ", humidity='" + humidity + '\'' +
//                '}';
//    }

    public String toString() {
        return "TimerInformation{" +
                "hour='" + hour + '\'' +
                "minute='" + minute + '\'' +
                '}';
    }
}
