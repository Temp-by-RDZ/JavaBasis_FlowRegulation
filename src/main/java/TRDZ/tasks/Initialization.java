package TRDZ.tasks;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Initialization	{

	public static final int CARS_COUNT = 4;

	public static final CyclicBarrier group = new CyclicBarrier(CARS_COUNT+1);
	public static final Semaphore space = new Semaphore(CARS_COUNT/2);
	public static final CountDownLatch Signal = new CountDownLatch(1);
	public static final Lock Finish = new ReentrantLock();

	//region Внесенные изменения в целях достижения поставленной задачи с минимальным внедрением в заданный код используя обьекты java.util.concurrent.
		/*
		1. Все участники должны стартовать одновременно вне зависимости от времени сбора.
			> Initialization (Main)	- ожидание сбора всех машин с помощью CyclicBarrier и после подача сигнала о выводе сообщения (CountDownLatch через countDown()).
			> Car (Run)				- ожидание сбора всех машин (через CyclicBarrier) и сигнала о начале гонки (через CountDownLatch).
									Это сделанно так как при единственном ожидании для всех не контролируется порядок высвобождения потоков
									из-за чего сообщение о начале гоонки могло выводится позже.
		2. В тонель может войти лишь половина машин за раз.
			> Stage_Tunnel (go)		- ограничение на количество взаимодействий с помощью Semaphore обращение и высвобождение.
		3. После последнего финишировавшего выводится сообщение о конце гонки.
			> Initialization (Main)	- ожидание сбора всех машин с помощью уже созданного CyclicBarrier.
			> Car (Run)				- ожидание сбора всех машин с помощью уже созданного CyclicBarrier.
		4. Первый пришедшие будет единственным победителем
			> Car (Run)				- Певый пришедший провзаимодействует с финишем (Lock) чем и закоет для остальных эту возможность.
		 */
	//endregion

	public static void main(String[] args) {
		System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
		Race race = new Race(new Stage_Road(60), new Stage_Tunnel(), new Stage_Road(40));
		Car[] cars = new Car[CARS_COUNT];
		for (int i = 0; i < cars.length; i++) {
			cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
			}
		for (int i = 0; i < cars.length; i++) {
			new Thread(cars[i]).start();
			}
	//region Добавляем ожидание готовности всех машин
		try {group.await(); }
		catch (InterruptedException | BrokenBarrierException e) {e.printStackTrace();}
	//endregion
		System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
	//region Подаем сигнал о том что сообщение красиво вывелось.
		Signal.countDown();
	//endregion
	//region Добавляем ожидание финиширования всех машин
			try {group.await(); }
			catch (InterruptedException | BrokenBarrierException e) {e.printStackTrace();}
	//endregion
		System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
		}

	}
