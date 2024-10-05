package utils;

import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DataGenerator {
    static Logger log = LogManager.getLogger(DataGenerator.class);
    static Faker faker=new Faker();
    public static String getFirstName(){
        String firstName=faker.name().firstName();
        return firstName;
    }
    public static String getLastName(){
        String lastName=faker.name().lastName();
        return lastName;
    }

    public static String getFullName(){
        String fullName=faker.name().fullName();
        return fullName;
    }

    public static String combinedName(){
        String combinedName=faker.name().firstName()+"  "+faker.name().lastName();
        return combinedName;
    }

    public static String getTitleOfAdvisor(){
        String titleOfOwner=faker.name().prefix();
        return titleOfOwner;
    }
    public static String getTitleOfBusiness(){
        String businessTitle=faker.company().name();
        return businessTitle;
    }
    public static String calculationDateDOB(String inputDate,int age){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate date = LocalDate.parse(inputDate, formatter);
        LocalDate newDate = date.minusYears(age);
        String formattedDate = newDate.format(formatter);
        log.info("get Date of birth="+formattedDate);
        return formattedDate;
    }
    public static String calculationDateDOBInMM(String inputDate,int age){
        LocalDate today = LocalDate.now();
        LocalDate yearAgo = today.minusYears(age);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = yearAgo.format(formatter);
        log.info("get Date of birth="+formattedDate);
        return formattedDate;
    }

    public static String getIRANumber(){
        String IRANumber=faker.idNumber().ssnValid();
        return IRANumber;
    }

    public static String getPhoneNumber(){
        String phoneNumber=faker.phoneNumber().cellPhone();
        return phoneNumber;
    }
    public static String getEmailID(){
        String emailID=faker.internet().emailAddress();
        return emailID;
    }
    public static String getTodaysDate(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
    public static String calculationDateDOBEJT(String inputDate,int age){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse(inputDate, formatter);
        LocalDate newDate = date.minusYears(age);
        String formattedDate = newDate.format(formatter);
        log.info("get Date of birth="+formattedDate);
        return formattedDate;
    }
}
