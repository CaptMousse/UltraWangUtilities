package wang.ultra.my_utilities.game2048;

import wang.ultra.my_utilities.common.utils.StringUtils;

import java.util.*;

public class Game2048Test {
    static Map<Integer, Integer> dashboard = new LinkedHashMap<>();

    public Map<Integer, Integer> getDashboard() {
        return dashboard;
    }

    public void initDashboard() {
        dashboard.put(11, 0);
        dashboard.put(12, 0);
        dashboard.put(13, 0);
        dashboard.put(14, 0);
        dashboard.put(21, 0);
        dashboard.put(22, 0);
        dashboard.put(23, 0);
        dashboard.put(24, 0);
        dashboard.put(31, 0);
        dashboard.put(32, 0);
        dashboard.put(33, 0);
        dashboard.put(34, 0);
        dashboard.put(41, 0);
        dashboard.put(42, 0);
        dashboard.put(43, 0);
        dashboard.put(44, 0);
    }

    public void printDashboard() {
        List<Integer> dashboardList = convertDashboardToList();

        System.out.println(dashboardList.get(0) + "\t" + dashboardList.get(1) + "\t" + dashboardList.get(2) + "\t" + dashboardList.get(3));
        System.out.println(dashboardList.get(4) + "\t" + dashboardList.get(5) + "\t" + dashboardList.get(6) + "\t" + dashboardList.get(7));
        System.out.println(dashboardList.get(8) + "\t" + dashboardList.get(9) + "\t" + dashboardList.get(10) + "\t" + dashboardList.get(11));
        System.out.println(dashboardList.get(12) + "\t" + dashboardList.get(13) + "\t" + dashboardList.get(14) + "\t" + dashboardList.get(15));
        System.out.println("----------------");
    }

    public Map<Integer, Integer> start() {
        int position = positionGenerator();
        while (!dashboard.get(position).equals(0)) {
            position = positionGenerator();
        }
        String l = String.valueOf(System.currentTimeMillis()).substring(12,13);
        int i = 2;
        if (l.equals("0")) {
            i = 4;
        }
        dashboard.put(position, i);

        return dashboard;
    }

    private Map<Integer, Integer> updateDashboard(List<Integer> newList) {

        List<Integer> dashboardList = convertDashboardToList();

        if (!dashboardList.equals(newList)) {
            dashboard.put(11, newList.get(0));
            dashboard.put(12, newList.get(1));
            dashboard.put(13, newList.get(2));
            dashboard.put(14, newList.get(3));
            dashboard.put(21, newList.get(4));
            dashboard.put(22, newList.get(5));
            dashboard.put(23, newList.get(6));
            dashboard.put(24, newList.get(7));
            dashboard.put(31, newList.get(8));
            dashboard.put(32, newList.get(9));
            dashboard.put(33, newList.get(10));
            dashboard.put(34, newList.get(11));
            dashboard.put(41, newList.get(12));
            dashboard.put(42, newList.get(13));
            dashboard.put(43, newList.get(14));
            dashboard.put(44, newList.get(15));

            start();
        }

        printDashboard();

        return dashboard;
    }

    public Map<Integer, Integer> actionUp() {

        List<Integer> dashboardList = convertDashboardToList();

        List<Integer> dashboardList1 = new ArrayList<>();
        List<Integer> dashboardList2 = new ArrayList<>();
        List<Integer> dashboardList3 = new ArrayList<>();
        List<Integer> dashboardList4 = new ArrayList<>();
        dashboardList1.add(dashboardList.get(0));
        dashboardList1.add(dashboardList.get(4));
        dashboardList1.add(dashboardList.get(8));
        dashboardList1.add(dashboardList.get(12));
        dashboardList2.add(dashboardList.get(1));
        dashboardList2.add(dashboardList.get(5));
        dashboardList2.add(dashboardList.get(9));
        dashboardList2.add(dashboardList.get(13));
        dashboardList3.add(dashboardList.get(2));
        dashboardList3.add(dashboardList.get(6));
        dashboardList3.add(dashboardList.get(10));
        dashboardList3.add(dashboardList.get(14));
        dashboardList4.add(dashboardList.get(3));
        dashboardList4.add(dashboardList.get(7));
        dashboardList4.add(dashboardList.get(11));
        dashboardList4.add(dashboardList.get(15));

        List<List<Integer>> dashboardAll = new ArrayList<>();
        dashboardAll.add(dashboardList1);
        dashboardAll.add(dashboardList2);
        dashboardAll.add(dashboardList3);
        dashboardAll.add(dashboardList4);

        List<Integer> resultList = new ArrayList<>();
        for (int init = 1; init<=16; init++) {
            resultList.add(0);
        }

        for (int line = 0; line < dashboardAll.size(); line++) {
            List<Integer> tempList = dashboardAll.get(line);

            actionCore(tempList);

            for (int i = 0; i < tempList.size(); i++) {
                int num = i * 4 + line;
                resultList.set(num, tempList.get(i));
            }
        }

        return updateDashboard(resultList);
    }

    public Map<Integer, Integer> actionDown() {
        List<Integer> dashboardList = convertDashboardToList();

        List<Integer> dashboardList1 = new ArrayList<>();
        List<Integer> dashboardList2 = new ArrayList<>();
        List<Integer> dashboardList3 = new ArrayList<>();
        List<Integer> dashboardList4 = new ArrayList<>();
        dashboardList1.add(dashboardList.get(12));
        dashboardList1.add(dashboardList.get(8));
        dashboardList1.add(dashboardList.get(4));
        dashboardList1.add(dashboardList.get(0));
        dashboardList2.add(dashboardList.get(13));
        dashboardList2.add(dashboardList.get(9));
        dashboardList2.add(dashboardList.get(5));
        dashboardList2.add(dashboardList.get(1));
        dashboardList3.add(dashboardList.get(14));
        dashboardList3.add(dashboardList.get(10));
        dashboardList3.add(dashboardList.get(6));
        dashboardList3.add(dashboardList.get(2));
        dashboardList4.add(dashboardList.get(15));
        dashboardList4.add(dashboardList.get(11));
        dashboardList4.add(dashboardList.get(7));
        dashboardList4.add(dashboardList.get(3));

        List<List<Integer>> dashboardAll = new ArrayList<>();
        dashboardAll.add(dashboardList1);
        dashboardAll.add(dashboardList2);
        dashboardAll.add(dashboardList3);
        dashboardAll.add(dashboardList4);

        List<Integer> resultList = new ArrayList<>();

        for (int init = 1; init<=16; init++) {
            resultList.add(0);
        }

        for (int line = 0; line < dashboardAll.size(); line++) {
            List<Integer> tempList = dashboardAll.get(line);

            actionCore(tempList);

            for (int i = 0; i < tempList.size(); i++) {
                int num = 16 - 4 * (i + 1) + line;
                resultList.set(num, tempList.get(i));
            }

        }

        return updateDashboard(resultList);
    }

    public Map<Integer, Integer> actionLeft() {
        List<Integer> dashboardList = convertDashboardToList();

        List<Integer> dashboardList1 = new ArrayList<>();
        List<Integer> dashboardList2 = new ArrayList<>();
        List<Integer> dashboardList3 = new ArrayList<>();
        List<Integer> dashboardList4 = new ArrayList<>();
        dashboardList1.add(dashboardList.get(0));
        dashboardList1.add(dashboardList.get(1));
        dashboardList1.add(dashboardList.get(2));
        dashboardList1.add(dashboardList.get(3));
        dashboardList2.add(dashboardList.get(4));
        dashboardList2.add(dashboardList.get(5));
        dashboardList2.add(dashboardList.get(6));
        dashboardList2.add(dashboardList.get(7));
        dashboardList3.add(dashboardList.get(8));
        dashboardList3.add(dashboardList.get(9));
        dashboardList3.add(dashboardList.get(10));
        dashboardList3.add(dashboardList.get(11));
        dashboardList4.add(dashboardList.get(12));
        dashboardList4.add(dashboardList.get(13));
        dashboardList4.add(dashboardList.get(14));
        dashboardList4.add(dashboardList.get(15));

        List<List<Integer>> dashboardAll = new ArrayList<>();
        dashboardAll.add(dashboardList1);
        dashboardAll.add(dashboardList2);
        dashboardAll.add(dashboardList3);
        dashboardAll.add(dashboardList4);

        List<Integer> resultList = new ArrayList<>();

        for (int init = 1; init<=16; init++) {
            resultList.add(0);
        }

        for (int line = 0; line < dashboardAll.size(); line++) {
            List<Integer> tempList = dashboardAll.get(line);

            actionCore(tempList);

            for (int i = 0; i < tempList.size(); i++) {
                int num = 4 * line + i;
                resultList.set(num, tempList.get(i));
            }

        }

        return updateDashboard(resultList);
    }

    public Map<Integer, Integer> actionRight() {
        List<Integer> dashboardList = convertDashboardToList();

        List<Integer> dashboardList1 = new ArrayList<>();
        List<Integer> dashboardList2 = new ArrayList<>();
        List<Integer> dashboardList3 = new ArrayList<>();
        List<Integer> dashboardList4 = new ArrayList<>();
        dashboardList1.add(dashboardList.get(3));
        dashboardList1.add(dashboardList.get(2));
        dashboardList1.add(dashboardList.get(1));
        dashboardList1.add(dashboardList.get(0));
        dashboardList2.add(dashboardList.get(7));
        dashboardList2.add(dashboardList.get(6));
        dashboardList2.add(dashboardList.get(5));
        dashboardList2.add(dashboardList.get(4));
        dashboardList3.add(dashboardList.get(11));
        dashboardList3.add(dashboardList.get(10));
        dashboardList3.add(dashboardList.get(9));
        dashboardList3.add(dashboardList.get(8));
        dashboardList4.add(dashboardList.get(15));
        dashboardList4.add(dashboardList.get(14));
        dashboardList4.add(dashboardList.get(13));
        dashboardList4.add(dashboardList.get(12));

        List<List<Integer>> dashboardAll = new ArrayList<>();
        dashboardAll.add(dashboardList1);
        dashboardAll.add(dashboardList2);
        dashboardAll.add(dashboardList3);
        dashboardAll.add(dashboardList4);

        List<Integer> resultList = new ArrayList<>();

        for (int init = 1; init<=16; init++) {
            resultList.add(0);
        }

        for (int line = 0; line < dashboardAll.size(); line++) {
            List<Integer> tempList = dashboardAll.get(line);

            actionCore(tempList);

            for (int i = 0; i < tempList.size(); i++) {
                int num = 4 * (line + 1) - i - 1;
                resultList.set(num, tempList.get(i));
            }

        }

        return updateDashboard(resultList);
    }

    private void actionCore(List<Integer> tempList) {

        // 原理
        // 先排序把0都放到后面
        // 再挨个相加
        // 然后再排序把0都放后面

        for (int n = 0; n < tempList.size() - 1; n++) {
            for (int i = 0; i < tempList.size() - 1; i++) {
                if (tempList.get(i) == 0) {
                    tempList.set(i, tempList.get(i + 1));
                    tempList.set(i + 1, 0);
                }
            }
        }

        for (int l = 0; l < tempList.size() - 1; l++) {
            if (tempList.get(l).equals(tempList.get(l + 1))) {
                tempList.set(l, tempList.get(l) + tempList.get(l + 1));
                tempList.set(l + 1, 0);
            }
        }

        for (int n = 0; n < tempList.size() - 1; n++) {
            for (int i = 0; i < tempList.size() - 1; i++) {
                if (tempList.get(i) == 0) {
                    tempList.set(i, tempList.get(i + 1));
                    tempList.set(i + 1, 0);
                }
            }
        }

    }

    private List<Integer> convertDashboardToList() {
        List<Integer> dashboardList = new ArrayList<>();
        dashboardList.add(dashboard.get(11));
        dashboardList.add(dashboard.get(12));
        dashboardList.add(dashboard.get(13));
        dashboardList.add(dashboard.get(14));
        dashboardList.add(dashboard.get(21));
        dashboardList.add(dashboard.get(22));
        dashboardList.add(dashboard.get(23));
        dashboardList.add(dashboard.get(24));
        dashboardList.add(dashboard.get(31));
        dashboardList.add(dashboard.get(32));
        dashboardList.add(dashboard.get(33));
        dashboardList.add(dashboard.get(34));
        dashboardList.add(dashboard.get(41));
        dashboardList.add(dashboard.get(42));
        dashboardList.add(dashboard.get(43));
        dashboardList.add(dashboard.get(44));

        return dashboardList;
    }


    /**
     * 新生成元素的坐标
     *
     * @return
     */
    private int positionGenerator() {
        String u = StringUtils.getMyUUID().substring(31, 32);
        int positionGenerator = Integer.parseInt(u, 16) + 1;

        int position;
        switch (positionGenerator) {
            case 1 -> position = 11;
            case 2 -> position = 12;
            case 3 -> position = 13;
            case 4 -> position = 14;
            case 5 -> position = 21;
            case 6 -> position = 22;
            case 7 -> position = 23;
            case 8 -> position = 24;
            case 9 -> position = 31;
            case 10 -> position = 32;
            case 11 -> position = 33;
            case 12 -> position = 34;
            case 13 -> position = 41;
            case 14 -> position = 42;
            case 15 -> position = 43;
            case 16 -> position = 44;
            default -> position = 0;
        }
        return position;
    }

    public static void main(String[] args) {
        Game2048Test game2048Test = new Game2048Test();
        game2048Test.start();

        game2048Test.actionRight();
    }
}
