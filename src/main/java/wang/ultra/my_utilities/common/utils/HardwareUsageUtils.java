package wang.ultra.my_utilities.common.utils;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HardwareUsageUtils {

    private static HardwareAbstractionLayer getHardwareAbstractionLayer() {
        SystemInfo systemInfo = new SystemInfo();
        return systemInfo.getHardware();
    }

    // 获取CPU使用率
    public Double getCpuUsage() {
        CentralProcessor processor = getHardwareAbstractionLayer().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];

        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;

//        return String.format("%.2f", ((1.0 - (idle * 1.0 / totalCpu)) * 100));
        return (1.0 - (idle * 1.0 / totalCpu)) * 100;
    }


    // 获取CPU温度

    public Double getCPUTemperature() {
//        return String.format("%.2f", getHardwareAbstractionLayer().getSensors().getCpuTemperature());
        return getHardwareAbstractionLayer().getSensors().getCpuTemperature();
    }

    // 获取内存使用率
    public Double getMemoryUsage() {
        SystemInfo sysInfo = new SystemInfo();
        GlobalMemory memory = sysInfo.getHardware().getMemory();
//        VirtualMemory virtualMemory = memory.getVirtualMemory();

        double memoryUsage = (memory.getTotal() - memory.getAvailable()) * 1.0 / memory.getTotal();

//        List<Double> list = new ArrayList<>();
//        list.add(memory.getTotal()));            // 内存总量
//        list.add(memory.getAvailable()));        // 可用内存
//        list.add(memoryUsage * 100); // 可用百分比
//        list.add(virtualMemory.toString());                 // 虚拟内存信息
        return memoryUsage * 100;
    }

    // 获取硬盘使用率
    public Double getDiskUsage() {

        File[] disks = File.listRoots();
        double diskUsage = (disks[0].getTotalSpace() - disks[0].getUsableSpace()) * 1.0 / disks[0].getTotalSpace();

        List<String> list = new ArrayList<>();
//        list.add(formatByte(disks[0].getTotalSpace()));     // 硬盘总空间
//        list.add(formatByte(disks[0].getUsableSpace()));    // 可用空间
//        list.add(String.format("%.2f", diskUsage * 100));   // 可用百分比

        return diskUsage * 100;
    }

    public static void main(String[] args) {
        HardwareUsageUtils hardwareUsageUtils = new HardwareUsageUtils();

        Double cpuUsage = hardwareUsageUtils.getCpuUsage();
        Double cpuTemperature = hardwareUsageUtils.getCPUTemperature();
        Double memoryUsage = hardwareUsageUtils.getMemoryUsage();
        Double diskUsage = hardwareUsageUtils.getDiskUsage();

        System.out.println("CPU Usage = " + cpuUsage);
        System.out.println("CPU Temperature = " + cpuTemperature);
        System.out.println("Memory Usage = " + memoryUsage);
        System.out.println("Disk Usage = " + diskUsage);

    }



}
