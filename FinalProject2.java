package com.mycompany.finalproject2;

import java.util.Scanner;

public class FinalProject2 {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        System.out.println("Select which simulation to run (1–17):");
        int choice = input.nextInt();

        // Parameters
        String filename = "";
        double b = 0;
        int h = 0;
        double m = 0;
        double T = 0;
        boolean isK = false;

        if (choice == 1) {
            filename = "connections_network1.txt";
            b = 2;
            h = 1;
            m = 0.05;
            T = 1.0;
            isK = true;

        } else if (choice == 2) {
            filename = "connections_random.txt";
            b = 2;
            h = 500;
            m = 0.10;
            T = 1.0;
            isK = true;

        } else if (choice == 3) {
            filename = "connections_random.txt";
            b = 2;
            h = 500;
            m = 0.90;
            T = 1.0;
            isK = true;

        } else if (choice == 4) {
            filename = "connections_random.txt";
            b = 2;
            h = 500;
            m = 0.60;
            T = 1.0;
            isK = true;

        } else if (choice == 5) {
            filename = "connections_random.txt";
            b = 2;
            h = 500;
            m = 0.60;
            T = 1.0;
            isK = true;

        } else if (choice == 6) {
            filename = "connections_random.txt";
            b = 2;
            h = 700;
            m = 0.30;
            T = 1.0;
            isK = true;

        } else if (choice == 7) {
            filename = "connections_random.txt";
            b = 2;
            h = 700;
            m = 0.30;
            T = 2.0;
            isK = true;

        } else if (choice == 8) {
            filename = "connections_random.txt";
            b = 2;
            h = 700;
            m = 0.30;
            T = 2.0;
            isK = true;

        } else if (choice == 9) {
            filename = "connections_lattice.txt";
            b = 2;
            h = 500;
            m = 0.10;
            T = 0.2;
            isK = false;

        } else if (choice == 10) {
            filename = "connections_lattice.txt";
            b = 2;
            h = 500;
            m = 0.10;
            T = 0.5;
            isK = false;

        } else if (choice == 11) {
            filename = "connections_lattice.txt";
            b = 2;
            h = 500;
            m = 0.10;
            T = 0.75;
            isK = false;

        } else if (choice == 12) {
            filename = "connections_lattice.txt";
            b = 3;
            h = 500;
            m = 0.10;
            T = 0.2;
            isK = false;

        } else if (choice == 13) {
            filename = "connections_lattice.txt";
            b = 3;
            h = 500;
            m = 0.50;
            T = 0.4;
            isK = false;

        } else if (choice == 14) {
            filename = "connections_lattice.txt";
            b = 1;
            h = 500;
            m = 0.90;
            T = 0.75;
            isK = false;

        } else if (choice == 15) {
            filename = "connections_lattice.txt";
            b = 1;
            h = 500;
            m = 0.90;
            T = 0.25;
            isK = false;

        } else if (choice == 16) {
            filename = "connections_lattice.txt";

            System.out.print("Enter b: ");
            b = input.nextDouble();

            System.out.print("Enter h: ");
            h = input.nextInt();

            System.out.print("Enter T: ");
            T = input.nextDouble();

            System.out.print("Does T depend on k? (1 = yes, 0 = no): ");
            isK = (input.nextInt() == 1);

            System.out.print("Enter m: ");
            m = input.nextDouble();

        } else if (choice == 17) {
            filename = "connections_random.txt";

            System.out.print("Enter b: ");
            b = input.nextDouble();

            System.out.print("Enter h: ");
            h = input.nextInt();

            System.out.print("Enter T: ");
            T = input.nextDouble();

            System.out.print("Does T depend on k? (1 = yes, 0 = no): ");
            isK = (input.nextInt() == 1);

            System.out.print("Enter m: ");
            m = input.nextDouble();

        } else {
            System.out.println("Using default test parameters.");
            filename = "connections_random.txt";
            b = 2;
            h = 0;
            m = 0.1;
            T = 1.0;
            isK = true;
        }

        // 🔹 Single call to simulation engine
        PartBDriver.runGame(filename, b, h, m, T, isK);
    }
}
