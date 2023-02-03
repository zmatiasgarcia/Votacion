package VotacionUNGS;
import java.util.HashMap;
import java.util.Map;

abstract public class Mesa {
	protected Integer codigo;
	protected Tupla<Integer, Integer> horario; //clave desde que hora hasta que hora
	protected Map<Integer, Persona> votantes;
	protected Map<Integer, Integer> turnos; //Clave horario, valor cantPersonas controla la cantidad de personas registradas por horario
	protected int capacidad; //Capacidad total de la mesa teniendo en cuenta todos los horarios
	protected int personasPorTurno;
	protected int dniPresidente;
	
	public Mesa(Integer codigo, Integer desde, Integer hasta, int dniPresidente, int capacidad, int personasPorTurno) {
		if (codigo == null) {
			throw new RuntimeException("Error: debe asignar un código");
		}
		this.dniPresidente = dniPresidente;
		this.codigo = codigo;
		this.votantes = new HashMap<Integer, Persona>();
		this.turnos = new HashMap<Integer,Integer>();
		this.horario = new Tupla<Integer, Integer>(desde, hasta);
		this.capacidad = capacidad;
		this.personasPorTurno = personasPorTurno;
	}
	
	public Tupla<Integer, Integer> asignarTurno(Persona p, String tipoDeMesa) {
		if (!cumpleRequisitos(tipoDeMesa)) {
			return null;
		}
		votantes.put(p.getDni(), p);
		return dameTurno();
	}
	
	private boolean cumpleRequisitos(String tipoDeMesa) {
		return hayCupo() && tipoDeMesa.equals(this.getClass().getSimpleName());
	}
	
	private Tupla<Integer, Integer> dameTurno() {
		int ultimoTurno = obtenerUltimoHorario();
		if (turnos.get(ultimoTurno) < personasPorTurno) {
			turnos.replace(ultimoTurno, turnos.get(ultimoTurno)+1);
			return new Tupla<Integer, Integer>(codigo, ultimoTurno);
		}
		else {
			turnos.put(ultimoTurno+1, 0);
			ultimoTurno = obtenerUltimoHorario();
			turnos.replace(ultimoTurno, turnos.get(ultimoTurno)+1);
			return new Tupla<Integer, Integer>(codigo, ultimoTurno);
		}
	}
	
	private int obtenerUltimoHorario() {
		int ultimoHorario = 0;
		for (Integer horario : turnos.keySet()) {
			ultimoHorario = horario;
		}
		return ultimoHorario;
	}
	
	public Tupla<Integer, Integer> darTurnoPresidente(Persona p) {
		votantes.put(p.getDni(), p);
		turnos.put(horario.getX(), 0);
		return dameTurno();
	}
	
	public boolean votar(int dni) {
		if (votantes.get(dni).isVoto()) {
			return false;
		}
		votantes.get(dni).setVoto(true);
		return true;
	}
	
	public boolean voto(int dni) {
		return votantes.get(dni).isVoto();
	}
	
	public String tipoDeMesa() {
		return getClass().getSimpleName();
	}
	
	public Persona getPersona(int dni) {
		Persona p = new Persona (votantes.get(dni).getDni(), votantes.get(dni).getNombre(), 
					votantes.get(dni).getEdad(), votantes.get(dni).isEnfPrevia(), votantes.get(dni).isTrabaja());
		p.setVoto(votantes.get(dni).isVoto());
		return p;
	}
	
	public int cantDeTurnos() {
		return votantes.size();
	}

	private boolean hayCupo() {
		return cantDeTurnos()<capacidad;
	}
	
	public boolean votaEnEstaMesa(Integer dni) {
		return votantes.containsKey(dni);
	}
	
	public boolean isEmpty() {
		return votantes.isEmpty();
	}

	public Integer getCodigo() {
		return codigo;
	}

	public Integer getDniPresidente() {
		return dniPresidente;
	}

	@Override
	public String toString() {
		StringBuilder bobElConstructor = new StringBuilder();
		bobElConstructor.append("Número de mesa: ").append(codigo).append("\n");
		bobElConstructor.append("Tipo de mesa: ").append(getClass().getSimpleName()).append("\n");
		bobElConstructor.append("Presidente de mesa: ").append(votantes.get(dniPresidente).toString()).append("\n");
		bobElConstructor.append("Cantidad de votantes: ").append(cantDeTurnos()).append("\n");
		bobElConstructor.append("-----------------------------------------------------------------------").append("\n");
		return bobElConstructor.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mesa other = (Mesa) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	
}
