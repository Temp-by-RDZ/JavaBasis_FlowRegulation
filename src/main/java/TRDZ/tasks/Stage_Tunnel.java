package TRDZ.tasks;

import static TRDZ.tasks.Initialization.space;

public class Stage_Tunnel extends Stage {

	public Stage_Tunnel() {
		this.length = 80;
		this.description = "Тоннель " + length + " метров";
		}

	@Override
	public void go(Car c) {
		try {
			try {
				System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
			//region Ограничиваем число единоразового обращения к этапу
				space.acquire();
			//endregion
				System.out.println(c.getName() + " начал этап: " + description);
				Thread.sleep(length / c.getSpeed() * 1000);
				}
			catch (InterruptedException e) { e.printStackTrace(); }
			finally {
				System.out.println(c.getName() + " закончил этап: " + description);
			//region Оповещаем о конце взаимодействия
				space.release();
			//endregion
				}
			}
		catch (Exception e) {	e.printStackTrace(); }
		}

	}
