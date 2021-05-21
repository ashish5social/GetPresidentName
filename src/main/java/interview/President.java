package interview;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class President {
    public static void main(String[] args) {

        List<Date> inputDates = null;

        try
        {
            File file=new File("input.txt");    //creates a new file instance
            FileReader fr=new FileReader(file);   //reads the file
            BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream
            inputDates = new ArrayList<Date>();
            String line;
            while((line=br.readLine())!=null)
            {
                Date d = new Date(line);
                inputDates.add(d);
            }
            fr.close();    //closes the stream and release the resource
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }



        System.setProperty("webdriver.chrome.driver", "/Users/kumar.ashish/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get("https://history.house.gov/Institution/Presidents-Coinciding/Presidents-Coinciding/");
        List<WebElement> allCells = driver.findElements(By.xpath("//div[@class='manual-table manual-table-not-sortable']/table/tbody/tr/td"));

        List<List<String>> listOfList = new ArrayList<List<String>>();

        int numRows = allCells.size() / 5 ;
        List<String> row = null;
        for(int i=0; i<numRows; i++) {
            //When i=0, this means its first row.
            row = new ArrayList<String>();
            for (int j=0; j<5; j++) {
                WebElement ele = allCells.get(i*5+j);
                String cell_i_j = ele.getText();
                row.add(cell_i_j);
            }
            listOfList.add(row);
        }


        driver.close();


        for (Date givenDate : inputDates) {
            System.out.println("Starting test for date " + givenDate);

            //I should keep a map of presidents where key is a pair of dates start and end and value is name of president.
            boolean flag=false;
            for (List<String> oneRow : listOfList) {
                String dateString = oneRow.get(3);
                String strStartDate = dateString.split("–")[0].replace(".", "");
                String strEndDate = dateString.split("–")[1].replace(".", "");

                Date startDate = new Date(strStartDate);
                Date endDate = null;
                if (strEndDate.equalsIgnoreCase("present")) {
                    endDate = new Date();
                } else {
                    endDate = new Date(strEndDate);
                }

                if (isGivenDateInBetween(givenDate, startDate, endDate)) {
                    System.out.println("Name of president on " + givenDate + " is " + oneRow.get(1));
                    flag=true;
                    break;
                }
            }
            if(flag == false)
                System.out.println("Given date is not valid");
        }

    }

    public static boolean isGivenDateInBetween(Date date, Date dateStart, Date dateEnd) {
        if (date != null && dateStart != null && dateEnd != null) {
            if (date.after(dateStart) && date.before(dateEnd)) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
}
