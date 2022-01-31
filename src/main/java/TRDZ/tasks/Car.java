package TRDZ.tasks;

import java.util.concurrent.BrokenBarrierException;

import static TRDZ.tasks.Initialization.*;


public class Car implements Runnable {

	private static int CARS_COUNT;
	private Race race;
	private int speed;
	private String name;

	public String getName() { return name; }
	public int getSpeed() {	return speed; }

	public Car(Race race, int speed) {
		this.race = race;
		this.speed = speed;
		CARS_COUNT++;
		this.name = "Участник #" + CARS_COUNT;
		}

	@Override
	public void run() {
		try {
			System.out.println(this.name + " готовится");
			Thread.sleep(500 + (int)(Math.random() * 800));
			System.out.println(this.name + " готов");
		//region Добавляем отсчет и ожидание сбора всех машин
		 	group.await();
			Signal.await();
		//endregion
			}
		catch (Exception e) { e.printStackTrace(); }
		try {Signal.await();}
		catch (InterruptedException e) {e.printStackTrace();}
		for (int i = 0; i < race.getStages().size(); i++) {
			race.getStages().get(i).go(this);
			}
	//region Добавляем дележку на победителя и прочих
		if (Finish.tryLock()) {
			Finish.lock();
			System.out.println(name+" ФИНИШИРОВАЛ ПЕРВЫМ! Он победил в гонке!");
			}
	//endregion
	//region Добавляем ожидание финиширования всех машин
		try {group.await(); }
		catch (InterruptedException | BrokenBarrierException e) {e.printStackTrace();}
	//endregion
		}
	}
