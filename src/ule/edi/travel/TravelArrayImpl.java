package ule.edi.travel;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ule.edi.model.*;

public class TravelArrayImpl implements Travel {
	
	private static final Double DEFAULT_PRICE = 100.0;
	private static final Byte DEFAULT_DISCOUNT = 25;
	private static final Byte CHILDREN_EXMAX_AGE = 18;
	private Date travelDate;
	private int nSeats;
	
	private Double price;    // precio de entradas 
	private Byte discountAdvanceSale;   // descuento en venta anticipada (0..100)
   	
	private Seat[] seats;
		
	
	
   public TravelArrayImpl(Date date, int nSeats){

	   this.nSeats = nSeats;
	   this.seats = new Seat[this.nSeats];
	   this.travelDate = date;
	   this.price = DEFAULT_PRICE;
	   this.discountAdvanceSale = DEFAULT_DISCOUNT;
   }
   
   
   public TravelArrayImpl(Date date, int nSeats, Double price, Byte discount){

	   this.travelDate = date;
	   this.nSeats = nSeats;
	   this.seats = new Seat[this.nSeats];
	   this.discountAdvanceSale = discount;
	   this.price = price; 
	}






@Override
public Byte getDiscountAdvanceSale() {
	
	return this.discountAdvanceSale;
}


@Override
public int getNumberOfSoldSeats() {

	int contador = 0;

	for(int i = 0; i < this.seats.length; i++) {
		if(this.seats[i] != null) {
			contador++;
		}
	}

	return contador;
}


@Override
public int getNumberOfNormalSaleSeats() {
	
	int contador = 0;
	
	for(int i = 0; i < this.seats.length; i++) {
		if(this.seats[i] != null) {
			if(!this.seats[i].getAdvanceSale()) {
				contador++;
			}
		}
	}

	return contador;
}


@Override
public int getNumberOfAdvanceSaleSeats() {
	
	int contador = 0;
	
	for(int i = 0; i < this.seats.length; i++) {
		if(this.seats[i] != null) {
			if(this.seats[i].getAdvanceSale()) {
				contador++;
			}
		}
	}

	return contador;

}


@Override
public int getNumberOfSeats() { 

	return this.nSeats;
}


@Override
public int getNumberOfAvailableSeats() {

	int contador = 0;
	
	for(int i = 0; i < this.seats.length; i++) {
		if(this.seats[i] == null) {
			contador++;
		}
	}
	
	return contador;
}

@Override
public Seat getSeat(int pos) {

	if((pos == 0) || (pos-1 >= this.seats.length) || (this.seats[pos-1] == null)) {
		return null;
	}

	Seat seat = new Seat(this.seats[pos-1].getAdvanceSale(), this.seats[pos-1].getHolder());
	return seat;	//si no esta vacio se devuelven los datos de seat (advanceSale y holder)
	 
}


@Override
public Person refundSeat(int pos) {
	
	if((pos == 0) || (pos-1 >= this.seats.length) || (this.seats[pos-1] == null)) {
		return null;
	}
	
	Person person = new Person(this.seats[pos-1].getHolder().getNif(), this.seats[pos-1].getHolder().getName(), this.seats[pos-1].getHolder().getAge());
	this.seats[pos-1] = null;	//si no esta vacio se guardan los datos y se borra el holder
	return person;	
}



private boolean isChildren(int age) {

	boolean child = false;
	
	if(age < CHILDREN_EXMAX_AGE && age >= 0) {
		child = true;
	}

	return child;
}

private boolean isAdult(int age) {
	
	boolean adult = false;

	if(age >= CHILDREN_EXMAX_AGE && age >= 0) {
		adult = true;
	}

	return adult;
}


@Override
public List<Integer> getAvailableSeatsList() {

	List<Integer> lista = new ArrayList<Integer>(this.nSeats);

	for(int i = 0; i < this.seats.length ; i++) {
		if(this.seats[i] == null) {
			lista.add(i+1);
		}
	}

	return lista;
}


@Override
public List<Integer> getAdvanceSaleSeatsList() {
	
	List<Integer> lista = new ArrayList<Integer>(this.nSeats);

	for(int i = 0; i < this.seats.length; i++) {
		if((this.seats[i] != null) && (this.seats[i].getAdvanceSale())) {
			lista.add(i+1);
		}
	}
	
	return lista;
}


@Override
public int getMaxNumberConsecutiveSeats() {
	
	int max = 0;
	int currentConsecutive = 0;

	for (int i = 0; i < this.seats.length; i++) {
		if(this.seats[i] == null) {
			currentConsecutive++;
			max = Math.max(max, currentConsecutive);
		} else {
			currentConsecutive = 0;
		}
	}
	return max;
}




@Override
public boolean isAdvanceSale(Person p) {

	boolean advance = false;

	for(int i = 0; i < this.seats.length; i++) {	//se busca al holder para poder saber si es de advance sale
		if((this.seats[i] != null) && (this.seats[i].getHolder().equals(p)) && (this.seats[i].getAdvanceSale())) {
			advance = true;
		}
	}

	return advance;
}


@Override
public Date getTravelDate() {
	
	return this.travelDate;
}


@Override
public boolean sellSeatPos(int pos, String nif, String name, int edad, boolean isAdvanceSale) {

	if((pos == 0) || (pos-1 >= this.seats.length) || (this.getPosPerson(nif) != -1)) {
		return false;
	}

	boolean available = false;	//si esta ocupado se devuelve false y no se puede vender

	if(this.seats[pos-1] == null) {	//si el asiento esta vacio se asigna un holder
		Person person = new Person(nif, name, edad);
		this.seats[pos-1] = new Seat(isAdvanceSale, person);
		available = true;
	}

	return available;
}


@Override
public int getNumberOfChildren() {

	int num = 0;
	
	for(int i = 0; i < this.seats.length; i++) {
		if((this.seats[i] != null) && (this.isChildren(this.seats[i].getHolder().getAge()))) {
			num++;
		}
	}
	return num;
}


@Override
public int getNumberOfAdults() {
	
	int num = 0;
	
	for(int i = 0; i < this.seats.length; i++) {
		if((this.seats[i] != null) && (this.isAdult(this.seats[i].getHolder().getAge()))) {
			num++;
		}
	}
	return num;
}



@Override
public Double getCollectionTravel() {
	
	double total = 0.0;

	for(int i = 0; i < this.seats.length; i++) {
		if(this.seats[i] != null) {
			total = total + this.getSeatPrice(this.seats[i]);
		}
	}

	return total;
}


@Override
public int getPosPerson(String nif) {
	
	int i=0;
	int asiento = -1;	//se devuelve -1 si no se encuentra
	boolean found = false;

	while ((i < this.seats.length) && (!found)) {		//se busca el nif y se devuelve el asiento donde esta
		if(this.seats[i] != null && this.seats[i].getHolder().getNif().equals(nif)) {
			asiento = i+1;
			found = true;
		} else {
			i++;
		}
	}

	return asiento;
}


@Override
public int sellSeatFrontPos(String nif, String name, int edad, boolean isAdvanceSale) {
	
	int i = 0;
	int seat = -1;	//si no hay asientos vacios se devuelve -1
	boolean found = false;
	
	while(i < this.seats.length && !found && this.getPosPerson(nif) == -1) {		//se busca el primer asiento vacio y se le asigna un holder
		if(this.seats[i] == null) {			
			Person person = new Person(nif, name, edad);
			this.seats[i] = new Seat(isAdvanceSale, person);
			seat = i+1;	
			found = true;
		} else {
			i++;
		}
	}

	return seat;
}


@Override
public int sellSeatRearPos(String nif, String name, int edad, boolean isAdvanceSale) {
	
	int i = this.nSeats-1;
	boolean found = false;
	int seat = -1;

	while(i >= 0 && !found && this.getPosPerson(nif) == -1) {
		if(this.seats[i] == null) {
			Person person = new Person(nif, name, edad);
			this.seats[i] = new Seat(isAdvanceSale, person);
			seat = i+1;
			found = true;
		} else {
		i--;
		}
	}

	return seat;
}




@Override
public Double getSeatPrice(Seat seat) {

	double money = 0.0;

	if(seat.getAdvanceSale()) {
		money = this.price - (this.price * this.discountAdvanceSale / 100);
	} else {
		money = this.price;
	} 
	return money;

}


@Override
public double getPrice() {

	return this.price;
}


}	