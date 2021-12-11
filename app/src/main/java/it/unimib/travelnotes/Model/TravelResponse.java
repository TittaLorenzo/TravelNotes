package it.unimib.travelnotes.Model;

public class TravelResponse {

    private Viaggio[] travel;

    public TravelResponse( Viaggio[] travel) {
        this.travel = travel;

    }

    public TravelResponse() {
    }


    public Viaggio[] getViaggi() {
        return travel;
    }

    public void setViaggi(Viaggio[] travel) {
        this.travel = travel;
    }




}
