package io.Risiko.Game.GameMain.Missions;

import io.Risiko.Utils.MiscUtils;

public class MissionUtils {

	private MissionUtils() {}
	
	public enum SecretMission {
		CaptureCont,
		CaptureTwelve,
		CaptureTwentyfour,
		Assassination
	}
	
	static public SecretMission getRandomSecretMission() {
		SecretMission[] temp = new SecretMission[] {SecretMission.CaptureCont, SecretMission.CaptureTwelve, SecretMission.CaptureTwentyfour, SecretMission.Assassination};
		return temp[MiscUtils.getRandomNumber(0, temp.length-1)];
	}
}
