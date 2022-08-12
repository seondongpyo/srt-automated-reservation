package io.github.seondongpyo.page;

public interface SrtTicketSearchablePage {

    SrtTicketListPage search(String departmentStation, String arrivalStation, String date, String time);
}
