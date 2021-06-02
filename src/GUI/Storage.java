package GUI;

import Parts.*;

//This class creates package wide variables as the program boots which are used to represent the components of the system
class Storage {

    //the objects representing each of the compnents of the system as well as the object responsible for stress testing the system
    final static CPU cpu = new CPU();
    final static GPU gpu = new GPU();
    final static RAM ram = new RAM();
    final static DiskList disks = new DiskList();
    final static Networking network = new Networking();
    final static OS system = new OS();
    final static Tester stress = new Tester();

    //The getter methods for each component
    protected static CPU getCpu() {
        return cpu;
    }

    protected static GPU getGpu() {
        return gpu;
    }

    protected static RAM getRam() {
        return ram;
    }

    protected static DiskList getDisks() {
        return disks;
    }

    protected static Networking getNetwork() {
        return network;
    }

    protected static OS getSystem() {
        return system;
    }

    protected static Tester getStress() {
        return stress;
    }
}
