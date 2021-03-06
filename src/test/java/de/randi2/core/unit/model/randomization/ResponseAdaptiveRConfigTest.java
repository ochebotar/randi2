package de.randi2.core.unit.model.randomization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;



import org.junit.Test;

import de.randi2.core.unit.randomization.RandomizationHelper;
import de.randi2.model.Trial;
import de.randi2.model.randomization.ResponseAdaptiveRConfig;
import de.randi2.randomization.RandomizationAlgorithm;
import de.randi2.testUtility.utility.AbstractDomainTest;


public class ResponseAdaptiveRConfigTest extends
		AbstractDomainTest<ResponseAdaptiveRConfig> {

	ResponseAdaptiveRConfig conf;
	
	public ResponseAdaptiveRConfigTest() {
		super(ResponseAdaptiveRConfig.class);
	}
	
	@Test
	public void testTrial_null(){
		conf = new ResponseAdaptiveRConfig();
		conf.setTrial(null);
		assertNull(conf.getTrial());
	}
    
	
	@Test
	public void testTrial_withEmptyRandomizationConfig(){
		conf = new ResponseAdaptiveRConfig();
		Trial trial = new Trial();
		assertNull(trial.getRandomizationConfiguration());
		conf.setTrial(trial);
		assertEquals(trial, conf.getTrial());
		assertEquals(conf, trial.getRandomizationConfiguration());
	}
	
	@Test
	public void testTrial_withRandomizationConfig(){
		conf = new ResponseAdaptiveRConfig();
		Trial trial = new Trial();
		trial.setRandomizationConfiguration(new ResponseAdaptiveRConfig());
		assertNotNull(trial.getRandomizationConfiguration());
		conf.setTrial(trial);
		assertEquals(trial, conf.getTrial());
		assertFalse(conf.equals(trial.getRandomizationConfiguration()));
	}
	
	@Test
	public void testGetAlgortihm_withSeed(){
		conf = new ResponseAdaptiveRConfig(1234l);
		conf.setTrial(new Trial());
		RandomizationAlgorithm<?> algorithm = conf.getAlgorithm();
		assertNotNull(algorithm);
		assertEquals(1234, algorithm.getSeed());
		assertEquals(algorithm, conf.getAlgorithm());
		
	}
	
	@Test
	public void testGetAlgortihm_withoutSeed(){
		conf = new ResponseAdaptiveRConfig();
		conf.setTrial(new Trial());
		RandomizationAlgorithm<?> algorithm = conf.getAlgorithm();
		assertNotNull(algorithm);
		assertEquals(Long.MIN_VALUE, algorithm.getSeed());
		assertEquals(algorithm, conf.getAlgorithm());
	}
	
	@Test
	public void testResetAlgorithm_withSeed(){
		conf = new ResponseAdaptiveRConfig(1234l);
		conf.setTrial(new Trial());
		RandomizationAlgorithm<?> algorithm = conf.getAlgorithm();
		assertEquals(1234, algorithm.getSeed());
		assertEquals(algorithm, conf.getAlgorithm());
		conf.resetAlgorithm();
		assertEquals(1234, conf.getAlgorithm().getSeed());
		assertNotSame(algorithm, conf.getAlgorithm());
	}
	
	
	@Test
	public void testResetAlgortihm_withoutSeed(){
		conf = new ResponseAdaptiveRConfig();
		conf.setTrial(new Trial());
		RandomizationAlgorithm<?> algorithm = conf.getAlgorithm();
		assertEquals(Long.MIN_VALUE, algorithm.getSeed());
		assertEquals(algorithm, conf.getAlgorithm());
		conf.resetAlgorithm();
		assertEquals(Long.MIN_VALUE, conf.getAlgorithm().getSeed());
		assertNotSame(algorithm, conf.getAlgorithm());
	}
	
	
	@Test
	public void testResetAlgorithmWithNextSeed(){
		conf = new ResponseAdaptiveRConfig(1234l);
		conf.setTrial(new Trial());
		RandomizationAlgorithm<?> algorithm = conf.getAlgorithm();
		assertEquals(1234, algorithm.getSeed());
		assertEquals(algorithm, conf.getAlgorithm());
		conf.resetAlgorithmWithNextSeed();
		assertEquals(1234+10000, conf.getAlgorithm().getSeed());
		assertNotSame(algorithm, conf.getAlgorithm());
	}
	
	
	@Test
	public void testResetAlgorithmWithNextSeed_withoutSeed(){
		conf = new ResponseAdaptiveRConfig();
		conf.setTrial(new Trial());
		RandomizationAlgorithm<?> algorithm = conf.getAlgorithm();
		assertEquals(Long.MIN_VALUE, algorithm.getSeed());
		assertEquals(algorithm, conf.getAlgorithm());
		conf.resetAlgorithmWithNextSeed();
		assertEquals(Long.MIN_VALUE, conf.getAlgorithm().getSeed());
		assertNotSame(algorithm, conf.getAlgorithm());
	}
	
	
	@Test
	public void testResetAlgorithmNewSeed(){
		conf = new ResponseAdaptiveRConfig();
		conf.setTrial(new Trial());
		RandomizationAlgorithm<?> algorithm = conf.getAlgorithm();
		assertEquals(Long.MIN_VALUE, algorithm.getSeed());
		assertEquals(algorithm, conf.getAlgorithm());
		conf.resetAlgorithm(123);
		assertEquals(123, conf.getAlgorithm().getSeed());
		assertNotSame(algorithm, conf.getAlgorithm());
	}
	
	
	@Test
	public void testCountBallsResponseSuccess(){
		conf = new ResponseAdaptiveRConfig();
		conf.setTrial(new Trial());
		conf.setCountBallsResponseSuccess(8);
		assertEquals(8, conf.getCountBallsResponseSuccess());
	}
	
	@Test
	public void testCountBallsResponseFailure(){
		conf = new ResponseAdaptiveRConfig();
		conf.setTrial(new Trial());
		conf.setCountBallsResponseFailure(2);
		assertEquals(2, conf.getCountBallsResponseFailure());
	}
	
	@Test
	public void testInitializeCountBallsResponseAdaptiveR(){
		conf = new ResponseAdaptiveRConfig();
		conf.setTrial(new Trial());
		conf.setInitializeCountBallsResponseAdaptiveR(10);
		assertEquals(10, conf.getInitializeCountBallsResponseAdaptiveR());
	}
	
	@Test
	public void testTempData(){
		conf = new ResponseAdaptiveRConfig();
		conf.setTempData(null);
		assertNotNull(conf.getTempData());
	}
	
	@Test
	public void testInvalidConfig(){
		conf = new ResponseAdaptiveRConfig();
		conf.setTrial(new Trial());
		RandomizationHelper.addArms(conf.getTrial(), 10, 15);
		conf.setCountBallsResponseFailure(6);	
		conf.setCountBallsResponseSuccess(4);
		assertInvalid(conf);
	}
	
	@Test
	public void testValidConfig(){
		conf = new ResponseAdaptiveRConfig();
		conf.setTrial(new Trial());
		RandomizationHelper.addArms(conf.getTrial(), 10, 10, 10);
		conf.setCountBallsResponseFailure(4);	
		conf.setCountBallsResponseSuccess(10);
		assertValid(conf);
	}
	
	
}
