package es.uco.pw.display.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import es.uco.pw.gestores.GestorUsuarios;
import es.uco.pw.data.Jugador;

/**
 * Menu principal para gestionar usuarios.
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */

public class MenuUsuarios{

	 @SuppressWarnings("resource")
	    /**
	     * Método principal para la gestión de usuarios.
	     * @param args Argumentos de línea de comandos
	     * @throws SQLException Excepción de SQL.
	     * @throws ParseException Excepción de análisis de fecha.
	     */
	 public static void main(String[] args){
		 Scanner myObj = new Scanner(System.in);   	                    
	     int codigoError, opcion = 99;
	     String nombreCompleto, correoElectronico, fechaNac;
	     Date fecha;

	     while (opcion != 0) {
	    	 System.out.println("Indique una opción");
	    	 System.out.println("1.- Añadir usuario");
	    	 System.out.println("2.- Modificar usuario");
	    	 System.out.println("3.- Listar usuarios registrados");
	    	 System.out.println("0.- Guardar y salir al menú superior");
	        	
	    	 opcion = Integer.parseInt(myObj.nextLine());

	    	 switch (opcion) {
	      	 	case 1: 
	      	 		//Añade un usuario                
	      	 		System.out.println("Indique nombre y apellidos del nuevo usuario (Formato: 1apellido,2apellido,nombre)");
	        		nombreCompleto = myObj.nextLine();
	                        
	        		System.out.println("Indique fecha de nacimiento del nuevo usuario (dd/mm/aaaa)");
	                fechaNac = myObj.nextLine();
	                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	                try {
	                    fecha = formato.parse(fechaNac);
	                } catch (ParseException e) {
	                    System.out.println("Error: Formato de fecha incorrecto.");
	                    break;
	                }
	        			
	        		System.out.println("Indique su correo electronico");
	        		correoElectronico = myObj.nextLine();
	        			
	        		Jugador newJugador = new Jugador(nombreCompleto, fecha, correoElectronico);     			
	        			
	        		codigoError = GestorUsuarios.insertarUsuario(newJugador);
	        		if(codigoError==1) {
	        			System.out.println("El usuario se ha guardado correctamente.");
	        		}
	        		else if(codigoError==-1) {
	        			System.out.println("El usuario no se ha guardado correctamente.");
	        		}
	        		else if(codigoError==-2) {
	        			System.out.println("El usuario ya ha sido introducido.");
	        		}
	     
	        		break;

	                        
	            case 2: 
	                //Modificar usuario
	                System.out.println("Indique el nombre completo del usuario a modificar");
	        		int nombreCompletoBusq = Integer.parseInt(myObj.nextLine());                        
	                   
	        			/**
	        			System.out.println("Indique nombre de usuario nuevo a modificar");
	        			nombre = myObj.nextLine();
		                        
	        			System.out.println("Indique apellido del nuevo asistente");
	        			apellido = myObj.nextLine();
		        			
	        			System.out.println("Indique fecha de nacimiento del nuevo asistente (dd/mm/aaaa)");
	        			fechaN = myObj.nextLine();
	        			formato = new SimpleDateFormat("dd/MM/yyyy");
	        			fecha = formato.parse(fechaN);
		
	        			atencionEspecial = 0;
	        			while (!(atencionEspecial == 1 || atencionEspecial == 2)) {
	        				System.out.println("Indique una opcion");
	        				System.out.println("1.- No requiere atención especial");
	        				System.out.println("2.- Requiere atención especial");
	        				atencionEspecial = Integer.parseInt(myObj.nextLine());
	        			}	        			
	        			if(atencionEspecial == 1){
	        				atencion = false;
	        			}
	        			else{
	        				atencion = true;
	        			}	 
		        			
	        			AsistentDTO modifyAsistent = new AsistentDTO(IDSearch, nombre, apellido, fecha, atencion);
	        			codigoError = GestorAsistentes.gestionarOpcion(2, modifyAsistent);
	        			if(codigoError==1) {
	        				System.out.println("El asistente se ha modificado correctamente.");
	        			}
	        			else if(codigoError==-1) {
	        				System.out.println("El asistente no se ha guardado correctamente.");
	        			}
	        			else if(codigoError==0) {
	        				System.out.println("No se puede establecer conexion con la base de datos.");
	        			}
	        			break;
	        			**/
	
	        	}	}
	        	}
}
