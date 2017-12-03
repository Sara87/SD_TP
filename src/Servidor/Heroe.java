package Servidor;

public class Heroe {

    private String name;
   // private boolean taken;

    public String getName() {
        return name;
    }

  /*  public boolean isTaken() {
        return taken;
    }*/

    public void setName(String name) {
        this.name = name;
    }

    /*public void setTaken(boolean taken) {
        this.taken = taken;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Heroe heroes = (Heroe) o;

        return name.equals(heroes.name);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
       // sb.append("Ocupado: ").append(taken);

        return sb.toString();
    }
}
