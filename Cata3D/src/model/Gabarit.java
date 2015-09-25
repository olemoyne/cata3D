package model;

import java.util.ArrayList;

import model.math.Decimal;
import model.math.Vecteur;
import tarpeia.com.model.TarpeiaField;
import tarpeia.com.model.TarpeiaObject;

/** Gabarit de construction **/
@TarpeiaObject
public class Gabarit {
	
	@TarpeiaField
	ArrayList<Vecteur> points;

	@TarpeiaField
	public Decimal position;
}
