package io.github.seondongpyo.page;

public interface SrtTicketSearchablePage {

    SrtTicketListPage search(String departmentStation, String arrivalStation, int count, String date, String time);
}
