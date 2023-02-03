package VotacionUNGS;

import java.util.ArrayList;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.util.List;


public class SistemaDeTurnos {
	private String nombre;
	private Map <Integer, Persona> personasRegistradas;
	private Map <Integer, Mesa> mesas;
	private Map <Integer, Tupla<Integer, Integer>> turnos;
	private int nmroMesas;
	
	public SistemaDeTurnos(String nombreSistema) {
		if (nombreSistema == null) {
			throw new RuntimeException("El sistema debe tener un nombre");
		}
		nombre = nombreSistema;
		personasRegistradas = new HashMap<Integer, Persona>();
		mesas = new HashMap<Integer, Mesa>();
		turnos = new HashMap<Integer, Tupla<Integer, Integer>>();
		nmroMesas = 0;
	}
	
	public void registrarVotante(int dni, String nombre, int edad, boolean enfPrevia, boolean trabaja) {
		if (edad < 16) {
			throw new RuntimeException("El votante es menor de 16 años");
		}
		if (nombre == null || nombre.equals("")) {
			throw new RuntimeException("Se debe asignar un nombre a la persona");
		}
		if (estaRegistrado(dni)) {
			throw new RuntimeException("La persona fue registrada previamente");
		}
		personasRegistradas.put(dni, new Persona(dni,nombre,edad,enfPrevia,trabaja));
	}
	
	public int agregarMesa(String tipoMesa, int dni) {
		if (!tipoMesa.equals("General") && !tipoMesa.equals("Enf_Preex") && !tipoMesa.equals("Mayor65") && !tipoMesa.equals("Trabajador")) {
			throw new RuntimeException("Tipo de mesa invalido: "+ tipoMesa);
		}
		if (!estaRegistrado(dni)) {
			throw new RuntimeException("La persona no esta registrada");
		}
		else if (tieneTurno(dni)) {
			throw new RuntimeException("La persona ya tiene turno asignado");
		}
		else{
			if (tipoMesa.equals("Enf_Preex")) {
				nmroMesas++;
				mesas.put(nmroMesas, new Enf_Preex(nmroMesas, dni));
				asignarPresidente(dni, nmroMesas);
				return nmroMesas;
			}
			else if (tipoMesa.equals("Mayor65")) {
				nmroMesas++;
				mesas.put(nmroMesas, new Mayor65(nmroMesas, dni));
				asignarPresidente(dni, nmroMesas);
				return nmroMesas;
			}
			else if (tipoMesa.equals("Trabajador")) {
				nmroMesas++;
				mesas.put(nmroMesas, new Trabajador(nmroMesas, dni));
				asignarPresidente(dni, nmroMesas);
				return nmroMesas;
			}
			nmroMesas++;
			mesas.put(nmroMesas, new General(nmroMesas, dni));
			asignarPresidente(dni, nmroMesas);
			return nmroMesas;
		}
	}
	
	public int asignarTurnos() {
		if (personasRegistradas.isEmpty()) {
			throw new RuntimeException("Error: no hay personas registradas");
		}
		int cont = 0;
		for (Integer p : personasRegistradas.keySet()) {
			if (!tieneTurno(p)) {
				asignarTurno(p);
				cont++;
			}
		}
		return cont;
	}
	public Tupla<Integer, Integer> asignarTurno(int dni) {
		if (!estaRegistrado(dni)) {
			throw new RuntimeException("La persona no esta registrada");
		}
		if (tieneTurno(dni)) {
			return consultaTurno(dni);
		}
		String cond = condicion(dni);
		for (Integer m : mesas.keySet()) {
			Tupla<Integer, Integer> t = consultarMesa(m).asignarTurno(consultarPersona(dni), cond);
			if (t != null) {
				turnos.put(dni, t);
				return t;
			}
		}
		return null;
	}
	
	private Tupla<Integer, Integer> asignarPresidente(int dni, int nmroMesa) {
		Tupla <Integer, Integer> nTurno = consultarMesa(nmroMesa).darTurnoPresidente(consultarPersona(dni));
		turnos.put(dni, nTurno);
		personasRegistradas.remove(dni);
		return nTurno;
	}

	private String condicion(int dni) {
		if (personasRegistradas.get(dni).isTrabaja()) {
			return "Trabajador";
		}
		else if (personasRegistradas.get(dni).isEnfPrevia()) {
			return "Enf_Preex";
		}
		else if (personasRegistradas.get(dni).isMayor65()) {
			return "Mayor65";
		}
		else {
			return "General";
		}
	}

	public Tupla<Integer, Integer> consultaTurno(int dni){
		if (!tieneTurno(dni)) {
			return null;
		} 
		return turnos.get(dni);
		}
	
	private Persona consultarPersona(int dni) {
		return personasRegistradas.get(dni);
	}

	private Mesa consultarMesa(int nmroMesa) {
		return mesas.get(nmroMesa);
	}

	public Map<Integer,List<Integer>> asignadosAMesa(int numMesa){
		if(!mesas.containsKey(numMesa)) {
			throw new RuntimeException("El numero de mesa es invalido");
		}
		if (consultarMesa(numMesa).isEmpty()) {
			return null;
		}
		Map<Integer, List<Integer>> personasAsignadas = new HashMap<Integer, List<Integer>>();
		for (Integer dni : turnos.keySet()) {
			if (consultarMesa(numMesa).votaEnEstaMesa(dni)) {
				if (personasAsignadas.containsKey(consultaTurno(dni).getY())) {
					personasAsignadas.get(consultaTurno(dni).getY()).add(dni);
				}
				else {
					personasAsignadas.put(consultaTurno(dni).getY(), new ArrayList<Integer>());
					personasAsignadas.get(consultaTurno(dni).getY()).add(dni);
				}
			}
		}
		return personasAsignadas;
	}

	public int votantesConTurno(String tipoMesa) {
		int cont =0;
		if(!tipoMesa.equals("Enf_Prexx") || !tipoMesa.equals("Mayor65") 
				|| !tipoMesa.equals("Trabajador") || !tipoMesa.equals("General")){
			throw new RuntimeException("La mesa" + tipoMesa +" no existe");
		}
		for (Integer mesa : mesas.keySet()) {
			if (consultarMesa(mesa).tipoDeMesa().equals(tipoMesa)) {
				cont = cont + consultarMesa(mesa).cantDeTurnos();
			}
		}
		return cont;
	}
	

	public boolean votar(int dni) {
		if (!estaRegistrado(dni)) {
			throw new RuntimeException("La persona no esta registrada");
		}
		else if (!tieneTurno(dni)) {
			throw new RuntimeException("La persona no tiene turno asignado");
		}
		for (Integer mesa : mesas.keySet()) {
			if (consultarMesa(mesa).votaEnEstaMesa(dni)) {
				if (voto(dni, mesa)) {
					return false;
				}
				else {
					return consultarMesa(mesa).votar(dni);
				}
			}
		}
		return false;
	}
	
	private boolean voto(int dni, int codMesa) {
		return consultarMesa(codMesa).voto(dni);
	}

	private Persona getPersona(int dni, int codMesa) {
		return consultarMesa(codMesa).getPersona(dni);
	}
	
	public List<Tupla<String,Integer>> sinTurnoSegunTipoMesa(){
		List<Tupla<String,Integer>> votantes = new ArrayList<Tupla<String,Integer>>();
		for (Integer dni : personasRegistradas.keySet()) {
			if (tieneLlave(votantes, condicion(dni))) {
				for (Tupla<String, Integer> tupla : votantes) {
					if (tupla.getX().equals(condicion(dni))) {
						tupla.setY(tupla.getY()+1);
					}
				}
			}
			else {
				votantes.add(new Tupla<String, Integer>(condicion(dni), 1));
			}
		}
		return votantes;
	}
	
	private boolean tieneLlave(List<Tupla<String,Integer>> lista, String llave) {
		if (lista.isEmpty()) {
			return false;
		}
		Iterator<Tupla<String,Integer>> it = lista.iterator();
		while (it.hasNext()){
			if (it.next().getX().equals(llave)){
				return true;
			}
		}
		return false;
	}
	
	private boolean estaRegistrado(int dni) {
		boolean esta = false;
		esta = esta || personasRegistradas.containsKey(dni);
		for (Integer mesa : mesas.keySet()) {
			esta = esta || consultarMesa(mesa).votaEnEstaMesa(dni);
		}
		return esta;
	}
	
	private boolean tieneTurno(int dni) {
		return turnos.containsKey(dni);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Sistema de Turnos para Votación - ").append(nombre).append("\n");
		sb.append(" - Personas a la espera de un turno: ").append("\n");
		for (Integer p : personasRegistradas.keySet()) {
			sb.append(personasRegistradas.get(p).toString()).append("\n");
		}
		sb.append("-----------------------------------------------------------------------").append("\n");
		sb.append(" - Personas con turno: ").append("\n");
		for (Integer p : turnos.keySet()) {
			sb.append(getPersona(p, consultaTurno(p).getX()).toString());
			sb.append(" - Codigo de mesa: ").append(turnos.get(p).getX());
			sb.append(" horario: ").append(turnos.get(p).getY());
			if (getPersona(p, consultaTurno(p).getX()).isVoto()) {
				sb.append(" - la persona ya efectuo su voto").append("\n");
			}
			else {
				sb.append(" - la persona no efectuo su voto").append("\n");
			}
		}
		sb.append("-----------------------------------------------------------------------").append("\n");
		sb.append(" - Mesas habilitadas: ").append(mesas.size()).append("\n");
		for (Integer m : mesas.keySet()) {
			sb.append(consultarMesa(m).toString());
		}
		return sb.toString();
	}
}
