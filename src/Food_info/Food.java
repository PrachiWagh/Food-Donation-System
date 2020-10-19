package Food_info;

public class Food {
    private String details;
    private Integer HoursToPerish;

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public void setHoursToPerish(Integer hoursToPerish) {
        HoursToPerish = hoursToPerish;
    }

    public Integer getHoursToPerish() {
        return HoursToPerish;
    }
}
