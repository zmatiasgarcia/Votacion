package VotacionUNGS;



public class Persona {
	private int dni;
	private int edad;
	private String nombre;
	private boolean enfPrevia;
	private boolean trabaja;
	private boolean voto;
	
	Persona(int dni, String nombre, int edad, boolean enfPrevia, boolean trabaja){
		if (dni < 0) {
			throw new RuntimeException("Error: el dni no puede ser "+ dni);
		}
		if (nombre == null) {
			throw new RuntimeException("Error: debe introducir un nombre");
		}
		if (edad < 16) {
			throw new RuntimeException("Error: el votante no debe ser menor de 16 años");
		}
		this.dni = dni;
		this.nombre = nombre;
		this.edad = edad;
		this.enfPrevia = enfPrevia;
		this.trabaja = trabaja;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public int getDni() {
		return dni;
	}

	public boolean isMayor65() {
		return edad >= 65;
	}

	public boolean isVoto() {
		return voto;
	}

	public void setVoto(boolean voto) {
		this.voto = voto;
	}


	public boolean isEnfPrevia() {
		return enfPrevia;
	}


	public boolean isTrabaja() {
		return trabaja;
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" Nombre: ").append(nombre);
		sb.append(" | ").append("DNI: ").append(dni);
		sb.append(" | ").append("Edad: ").append(edad);
		if (trabaja) {
			sb.append(" | ").append("trabajara el día de la votación");
		}
		else {
			sb.append(" | ").append("no trabajara el día de la votación");
		}
		if (enfPrevia) {
			sb.append(" | ").append("padece de alguna enfermedad preexistente ");
		}
		else {
			sb.append(" | ").append("no padece de alguna enfermedad preexistente ");
		}
		return sb.toString();
	}

	public int getEdad() {
		return edad;
	}
	
}
