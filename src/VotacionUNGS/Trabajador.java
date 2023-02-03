package VotacionUNGS;

public class Trabajador extends Mesa {

	public Trabajador(Integer codigo, int dniPresidente) {
		super(codigo, 8, 12, dniPresidente, -1, -1);
	}
	
	@Override
	public Tupla<Integer, Integer> asignarTurno(Persona p, String tipoDeMesa) {
		if (!cumpleRequisitos(tipoDeMesa)) {
			return null;
		}
		votantes.put(p.getDni(), p);
		turnos.replace(horario.getX(), turnos.get(horario.getY()+1));
		return new Tupla<Integer, Integer>(codigo, turnos.get(horario.getX()));
	}
	
	private boolean cumpleRequisitos(String tipoDeMesa) {
		return tipoDeMesa.equals(this.getClass().getSimpleName());
	}
}
