public class Runner {
    public static void main(String[] args) {
        try {
            Stock Test = new Stock("AAPL","5","year1month1");
            double d = Test.AvgCurrentDayPrice();
            System.out.println(Test.CurrentDayDeviation());
            System.out.println(d);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
