public class Movie implements Printable  {
    private String title;
    private String genre;
    private int duration; // in minutes
    private int id;

    public Movie(String title, String genre, int duration) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
    }

    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getDuration() { return duration; }
    @Override
    public void printDetails() {
        System.out.println("Movie ID: " + id);
        System.out.println("Title: " + title);
        System.out.println("Genre: " + genre);
        System.out.println("Duration: " + duration + " minutes");
    }
}
