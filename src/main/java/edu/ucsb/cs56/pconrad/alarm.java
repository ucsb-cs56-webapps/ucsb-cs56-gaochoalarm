import com.google.gson.Gson;

class alarm{
    private String time;
    private String purpose;

    public alarm(String time, String purpose){
        this.time = time;
        this.purpose = purpose;
    }

    @Override
    public String toString(){
        return "Time: " + time + "\nPurpose: " + purpose + "\n";
    }

    @Override
    public boolean equals(Object o){
        if (o == null)
			return false;
		if (!(o instanceof alarm))
            return false;
        alarm a = (alarm) o;
        return time == a.time && purpose == a.purpose;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static alarm toClass(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, alarm.class);
    }
}