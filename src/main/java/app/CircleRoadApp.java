package app;

import java.util.List;
import java.util.Random;

import app.component.CircleComp;
import app.config.AppConfig;
import app.domain.DTO.CarDTO;
import app.service.CarGenerationService;
import app.service.CarMovingService;
import app.service.RoadGenerationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {AppConfig.class})
public class CircleRoadApp {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(CircleRoadApp.class);
        run(context);
    }

    private static void run(ApplicationContext context) {
        RoadGenerationService roadService = context.getBean(RoadGenerationService.class);
        CarGenerationService carGenerationService = context.getBean(CarGenerationService.class);
        CarMovingService carMovingService = context.getBean(CarMovingService.class);

        int count = carGenerationService.generateCars();

        roadService.generateRoad(count);

        System.out.println("Roads, Circle and Car are Generated");
        System.out.println("Count of roads and car = " + count);



        while(carMovingService.getAllAutomobiles().size() != 0) {

            System.out.println("Before moving: ");
            printCarsToConsole(carMovingService.getAllAutomobiles());

            carMovingService.moveAllCars();

            System.out.println("After moving: ");
            printCarsToConsole(carMovingService.getAllAutomobiles());


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printCarsToConsole(List<CarDTO> cars) {
        System.out.println("Automobile list:");
        cars.forEach(auto -> {
            System.out.println("auto with ID " + auto.getId() + " stands on road block with ID "
                    + auto.getRoadBlock().getId().toString() + "on circle" + auto.getRoadBlock().getIsCircle() +"\n");
        });
    }

}
