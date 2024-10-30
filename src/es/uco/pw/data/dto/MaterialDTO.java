package es.uco.pw.data.dto;

/**
 * Clase que representa el material que puede ser reservado junto a la pista .
 * 
 *  @author Antonio Diaz Barbancho
 *	@author Carlos Marín Rodríguez 
 *	@author Carlos De la Torre Frias (GM2)
 *	@author Daniel Grande Rubio (GM2)
 * @since 08-10-2024
 * @version 1.0
 */

public class MaterialDTO{
	
	/**
	 * Identificador del material a reservar
	 */
	private int idMaterial;
	
	/**
	 * Tipo de material pelotas, canastas y conos
	 */
	private TipoMaterial tipoMaterial;
	
	/**
	 * Uso del inteior, true para pistas de interior y false para exterior
	 */
	private boolean usoInterior;
	
	/**
	 * Estado del material si esta disponible, reservado o en mal estado
	 */
    private EstadoMaterial estadoMaterial;
	
    /**
     * Enumeración que representa los diferentes estados posibles de un material.
     * Este enum permite categorizar el estado del material dentro del sistema,
     * facilitando así la gestión y el control de los recursos disponibles.
     */
    public enum TipoMaterial {
    	
    	/**
    	 * Material pelota
    	 */
        PELOTAS,
        
        /**
    	 * Material canastas
    	 */
        CANASTAS,
        
        /**
    	 * Material cono
    	 */
        CONOS
    }
    
    /**
     * Enumeración que representa los diferentes estados posibles de un material.
     * Esta enumeración permite clasificar el estado del material dentro del sistema.
     */
    public enum EstadoMaterial {
    	
    	/**
    	 * Estado disponible
    	 */
        DISPONIBLE,
        
        /**
    	 * Estado reservado
    	 */
        RESERVADO,
        
        /**
    	 * Estado correspondiente a mal estado del material
    	 */
        MAL_ESTADO
    }
    
    /**
	 * Constructor vacio de la clase Material
	 */
    public MaterialDTO() {
    	
    }
    
    /**
     * Constructor de la clase material
     * @param idMaterial Para el identificador del material
     * @param tipoMaterial Para el tipo del material
     * @param usoInterior Para indicar el uso interor o exterior
     * @param estadoMaterial Para indicar el estado del material
     * 
 	*/
    public MaterialDTO(int idMaterial, TipoMaterial tipoMaterial, boolean usoInterior, EstadoMaterial estadoMaterial) {
        this.idMaterial = idMaterial;
        this.tipoMaterial = tipoMaterial;
        this.usoInterior = usoInterior;
        this.estadoMaterial = estadoMaterial;
    }
    
    /**
     * Devuelve el id del material
     * @return idMaterial El id del material
 	*/
    public int getIdMaterial() {
		return idMaterial;
	}

	/**
	 * Modifica el id del Material
	 * @param idMaterial Id del material
	 */
	public void setIdMaterial(int idMaterial) {
		this.idMaterial = idMaterial;
	}

	/**
	 * Devuelve el uso del material
	 * @return usoMaterial El uso del material
	 */
	public boolean getUsoInterior() {
		return usoInterior;
	}

	/**
	 * Modifica el uso del interior
	 * @param usoInterior Uso interior 
	 */
	public void setUsoInterior(boolean usoInterior) {
		this.usoInterior = usoInterior;
	}

	/**
	 * Devuelve el estado del material
	 * @return estadoMaterial Estado del material
	 */
	public EstadoMaterial getEstadoMaterial() {
		return estadoMaterial;
	}

	/**
	 * Modifica el estado del material
	 * @param estadoMaterial Estado del material
	 */
	public void setEstadoMaterial(EstadoMaterial estadoMaterial) {
		this.estadoMaterial = estadoMaterial;
	}
	
	/**
	 * Modifica el tipo de material
	 * @param tipoMaterial Tipo de material
	 */
	 public void setTipoMaterial(TipoMaterial tipoMaterial) {
	        this.tipoMaterial = tipoMaterial;
	    }
	 
	 /**
		 * Devuelve el tipo de material
		 * @return Devuelve el tipo de material
		 */
	 public TipoMaterial getTipoMaterial() {
	        return tipoMaterial;
	    }
	
	/*
	    * Muestra la informacion referente al Material
	    * @param none
	    * @return string La informacion del material
		*/
	@Override
	public String toString() {
        return "Material [ID: " + idMaterial + ", Tipo: " + tipoMaterial + ", Uso Interior: " + usoInterior + ", Estado: " + estadoMaterial + "]";
    }
}