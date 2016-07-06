package gabriele;

import java.util.ArrayList;
import java.util.Iterator;

import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.util.collections.IndexedIterable;
import repast.simphony.engine.schedule.DefaultActionFactory;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.util.collections.IndexedIterable;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
//import repast.simphony.random.RandomHelper;
//import gabriele.LaborMkt;
import gabriele.agents.Consumer;
import gabriele.agents.Firm;
import gabriele.institutions.LaborMarket;
import gabriele.institutions.OfficeForStatistics;
import gabriele.agents.Bank;

public class Context implements ContextBuilder<Object> {
	public static boolean verboseFlag=true;
	public static boolean schedulingFlag=true;

	public static boolean saveMacroData=true;
	public static boolean saveMicroData=true;
	public static boolean timeStampInFileName=false;

	public static double minAbilityStudent=0.35;
	public static double maxAbilityStudent=0.5;
	public static double minPreferenceParameter=1.0;
	public static double maxPreferenceParameter=1.5;
	public static double consumersProbabilityToGetFunded=0.5;
	public static double percentageOfCreditAllowedToConsumersWhenCreditIsNotTotallyFunded=0;
	public static double firmsProbabilityToHaveOutstandingDebtCompletelyRenewed=0.5;
	public static double percentageOfOutstandingCreditAllowedToFirmsWhenCreditIsNotCompletelyRenewed=0.9;
	public static double firmsProbabilityToHaveNewDemandedCreditCompletelyAllowed=0.5;
	public static double percentageOfNewDemandedCreditAllowedToFirmsWhenCreditIsNotCompletelyAllowed=0.9;

	public static int numConsumers = 10;
	public static int numFirms = 3;
	public static int numBanks = 1;
	public static int consumerExitAge=70;
	public static int maxNumberOfFailedPeriodsOfEducation=2;
	public static int maxNumberPeriodsOfEducation=21;
	public static double probabilityToBeUnemployedAtTheBeginning=0.2;
	public static int parameterOfProductivityInProductionFuncion=100;
//	public static int parameterOfnumberOfWorkersToDetermineProductionCapitalInProductionFuncion=50;
	public static int minConsumerInitialBankAccount=-500;
	public static int maxConsumerInitialBankAccount=500;
	public static double minFirmInitialEquityRatio=0.1;
	public static double maxFirmInitialEquityRatio=0.3;
	public static double minBankInitialEquityRatio=0.1;
	public static double maxBankInitialEquityRatio=0.3;
	public static double probabilityToBeAskedToRefundForIndebtedWorkers=0.5;
	public static double percentageOfLoanToRefundForIndebtedWorkersIfAsked=0.1;
	public static int productionOfNewEnteringFirm=50;
	public static double percentageOfDemandMissedBecauseOfGoodsMarketsInperfections=0.0;
	public static double percentageOfUsedCapitalDepreciation=0.01;
	public static double percentageOfUnusedCapitalDepreciation=0.0;
	public static double percentageOfRealizedUnusedProductionCapital=0.9;
	public static double laborMarketStateToSetWage=0.5;
	public static double interestRateOnDeposits=0.001;
	public static double interestRateOnLoans=0.004;
	public static double interestRateOnSubsidizedLoans=0.001;
	public static int unemploymentDole=10;
	public static int subsistenceConsumption=10;
	public static int costEdu=10;
	// set the wageSettingRule variable to
	//	0 if you want the wage depend on the worker productivity
	//	1 if you want the wage depend on the average productivity of workers having the worker degree in the employer's firm
	//	2 if you want the wage depend on the average productivity of workers having the worker degree in the economy
	public static int wageSettingRule=2;


	//set the firmsWorkersMatching variable to
	//	0 do the following: 	unemployed send applications to firms
	//				firms employ by looking at the received applications
	//	1 do the following:	unemployed send applications to firms and to the office for labor
	//				firms employ by looking at the received applications. If they need additional workers ask to the office for labor
	//	2 do the following:	unemployed send applications to the office for labor only
	//				firms post their vacancies to the office for labor only
	//				the office for labor match vacancies an unemployed

	public static int firmsWorkersMatching=0;

	public static int numberOfJobApplicationAnUneployedSends=2;
	public static int numberOfBanksAConsumerCanBeCustumerOf=1;
	public static int numberOfBanksAFirmCanBeCustumerOf=1;
	public static int consumersProgressiveIdentificationNumber=0; 
	public static int firmsProgressiveIdentificationNumber=0; 
	public static int banksProgressiveIdentificationNumber=0; 

	/*
	   double initialProbabilityToBeEmployed=0.7;
	   VARIABILI USATE IN LAB MKT
	   double reservationWageWorker;
	   double workerWage;
	   int numApplic;
	   EMPLOYMENT STATUS: DECIDERE SE TENERE O RIMUOVERE IN BASE A LAB MKT
	   int status;
	   */


	//propensioni rivedere calibration
	public static double alpha = 0.4;
	public static double beta = 0.4;
	//taxes on consumption, tax rate evolves??...T = \phi c
	public static double taxRate = 0.1;


	Consumer aConsumer;
	Firm aFirm;
	Bank aBank;

	IndexedIterable consumersList,firmsList,banksList;
	OfficeForStatistics officeForStatistics;

	DefaultActionFactory contextActionFactory= new DefaultActionFactory();
	IAction contextAction;

	public static ISchedule schedule;

//	public boolean debtCancelledOnfinancialDifficulty;
//	public double betaStud,betaWorker;

	//public static boolean verbouseFlag=true;
	public repast.simphony.context.Context<Object> build(
			repast.simphony.context.Context<Object> context) {

		//int maxIter=30;

//		RandomHelper.setSeed(1469873);

	Parameters params = RunEnvironment.getInstance().getParameters();
	verboseFlag=(boolean)params.getValue("verboseFlag");
        numConsumers = (Integer)params.getValue("numConsumers");
        numFirms = (Integer)params.getValue("numFirms");
	minAbilityStudent=(Double)params.getValue("minAbilityStudent");
	maxAbilityStudent=(Double)params.getValue("maxAbilityStudent");
        parameterOfProductivityInProductionFuncion = (Integer)params.getValue("parameterOfProductivityInProductionFuncion");
	maxNumberOfFailedPeriodsOfEducation=(Integer)params.getValue("maxNumberOfFailedPeriodsOfEducation");
	probabilityToBeUnemployedAtTheBeginning=(Double)params.getValue("probabilityToBeUnemployedAtTheBeginning");
	numberOfBanksAConsumerCanBeCustumerOf=(Integer)params.getValue("numberOfBanksAConsumerCanBeCustumerOf");
	minConsumerInitialBankAccount=(Integer)params.getValue("minConsumerInitialBankAccount");
	maxConsumerInitialBankAccount=(Integer)params.getValue("maxConsumerInitialBankAccount");
	numberOfBanksAFirmCanBeCustumerOf=(Integer)params.getValue("numberOfBanksAFirmCanBeCustumerOf");
	minFirmInitialEquityRatio=(Double)params.getValue("minFirmInitialEquityRatio");
	maxFirmInitialEquityRatio=(Double)params.getValue("maxFirmInitialEquityRatio");
	minBankInitialEquityRatio=(Double)params.getValue("minBankInitialEquityRatio");
	maxBankInitialEquityRatio=(Double)params.getValue("maxBankInitialEquityRatio");
//        debtCancelledOnfinancialDifficulty = (Boolean)params.getValue("debtCancelledOnfinancialDifficulty");
//        betaStud = (Double)params.getValue("betaStud");
//        betaWorker = (Double)params.getValue("betaWorker");

        unemploymentDole=(Integer)params.getValue("unemploymentDole");
        subsistenceConsumption=(Integer)params.getValue("subsistenceConsumption");
	costEdu=(Integer)params.getValue("studentConsumption");
        wageSettingRule=(Integer)params.getValue("wageSettingRule");
        int batchStoppingTime=(Integer)params.getValue("batchStoppingTime");        
	//			startRecordingConsumersData=(Integer)params.getValue("startRecordingConsumersData");
        //			intervalInRecordingConsumersData=(Integer)params.getValue("intervalInRecordingConsumersData");

	if(verboseFlag){
			System.out.println("");
			System.out.println("");
			System.out.println("BEGINNING OF INITIAL SETUP");
			System.out.println("==========================================");
			System.out.println("");
			System.out.println("");
		}


		if(verboseFlag){
			System.out.println("CREATING CONSUMERS");
		}
		for (int i = 0; i< numConsumers; i++){
			aConsumer=new Consumer(consumersProgressiveIdentificationNumber,context);
			consumersProgressiveIdentificationNumber++;
			aConsumer.initialize();
			context.add(aConsumer);
		}

		if(verboseFlag){
			System.out.println("CREATING FIRMS");
		}

		for (int f = 0; f<numFirms; f++){
			context.add(new Firm(firmsProgressiveIdentificationNumber,context));
			firmsProgressiveIdentificationNumber++;
		}

		if(verboseFlag){
			System.out.println("CREATING BANKS");
		}

		for (int b = 0; b<numBanks; b++){
			context.add(new Bank(banksProgressiveIdentificationNumber,context));
			banksProgressiveIdentificationNumber++;
		}



		if(verboseFlag){
			System.out.println("CREATING OFFICE FOR LABOR");
		}
		LaborMarket theLaborMarket=new LaborMarket();
		context.add(theLaborMarket);

		try{
			consumersList=context.getObjects(Class.forName("gabriele.agents.Consumer"));
			firmsList=context.getObjects(Class.forName("gabriele.agents.Firm"));
			banksList=context.getObjects(Class.forName("gabriele.agents.Bank"));
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}



		if(verboseFlag){
			System.out.println("CREATING OFFICE FOR STATISTICS");
		}
		officeForStatistics=new OfficeForStatistics(context);


		if(verboseFlag){
			System.out.println("CONSUMERS: SEND CVs");
		}

		for(int i=0;i<consumersList.size();i++){
			aConsumer=(Consumer)consumersList.get(i);
			aConsumer.sendInitialJobApplication();
		}


		if(verboseFlag){
			System.out.println("FIRMS: HIRE");
		}
		for(int i=0;i<firmsList.size();i++){
			aFirm=(Firm)firmsList.get(i);
			aFirm.setInitialWorkers();
		}

		if(verboseFlag){
			System.out.println("CONTEXT: REMOVING FIRMS WITH ZERO PRODUCTION");
		}
		ArrayList<Firm> firmsToRemove=new ArrayList<Firm>();
		Iterator contextIterator=context.iterator();
		while(contextIterator.hasNext()){
			Object anObj=contextIterator.next();
			if(anObj instanceof Firm){
				aFirm=(Firm)anObj;
				if(aFirm.getNumberOfWorkers()<1){
					if(verboseFlag){
						System.out.println("     firm "+aFirm.getIdentity()+" removed because producing "+aFirm.getProduction());
					}
					firmsToRemove.add(aFirm);
				}
			}
		}

		for(int z=0;z<firmsToRemove.size();z++){
			context.remove(firmsToRemove.get(z));
		}
		//		try{
		IndexedIterable firmsList=context.getObjects(Firm.class);
		//		}
		//		catch(ClassNotFoundException e){
		//			System.out.println("Class not found");
		//		}

		if(firmsList.size()<1){
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println(":-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(");
			System.out.println(":-(   :-(                                                                                                         :-(   :-(");
			System.out.println(":-(   :-(    SIMULATION STOPPED BECAUSE NO FIRM HAS POSITIVE PRODUCTION: PLEASE VERIFY YOUR PARAMETRIZATION!      :-(   :-(");
			System.out.println(":-(   :-(                                                                                                         :-(   :-(");
			System.out.println(":-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(   :-(");
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			System.exit(0);		
		}



		if(verboseFlag){
			System.out.println("CONSUMERS SETUP BANK ACCOUNTS");
		}
		contextAction=contextActionFactory.createActionForIterable(consumersList,"setupBankAccount",false);
		contextAction.execute();

		if(verboseFlag){
			System.out.println("FIRMS SETUP BANK ACCOUNTS");
		}
		contextAction=contextActionFactory.createActionForIterable(firmsList,"setupBankAccountInInitialization",false);
		contextAction.execute();

		if(verboseFlag){
			System.out.println("BANK SETUP BALANCE");
		}
		contextAction=contextActionFactory.createActionForIterable(banksList,"computeDemandedCredit",false);
		contextAction.execute();
		contextAction=contextActionFactory.createActionForIterable(banksList,"computeDeposits",false);
		contextAction.execute();
		contextAction=contextActionFactory.createActionForIterable(banksList,"setupBalance",false);
		contextAction.execute();
		contextAction=contextActionFactory.createActionForIterable(banksList,"computeBalanceVariables",false);
		contextAction.execute();


		if(verboseFlag){
			System.out.println("CHECK BALANCE SHEET CONSISTENCY");
		}

		contextAction=contextActionFactory.createActionForIterable(consumersList,"computeWealth",false);
		contextAction.execute();
		contextAction=contextActionFactory.createActionForIterable(firmsList,"computeSumOfBankAccounts",false);
		contextAction.execute();

		try{
			consumersList=context.getObjects(Class.forName("gabriele.agents.Consumer"));
			firmsList=context.getObjects(Class.forName("gabriele.agents.Firm"));
			banksList=context.getObjects(Class.forName("gabriele.agents.Bank"));
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}

		double sumOfConsumersAndFirmsFinancialBalances=0;

		for(int i=0;i<consumersList.size();i++){
			aConsumer=(Consumer)consumersList.get(i);
			sumOfConsumersAndFirmsFinancialBalances+=aConsumer.getWealth();
		}
		for(int i=0;i<firmsList.size();i++){
			aFirm=(Firm)firmsList.get(i);
			sumOfConsumersAndFirmsFinancialBalances+=aFirm.getSumOfBankAccounts();
		}
		double sumOfBanksEquity=0;
		for(int i=0;i<banksList.size();i++){
			aBank=(Bank)banksList.get(i);
			sumOfBanksEquity+=aBank.getEquity();
		}



		if(verboseFlag){
			System.out.println("");
			System.out.println("     sumOfConsumersAndFirmsFinancialBalances "+sumOfConsumersAndFirmsFinancialBalances);
			System.out.println("     sumOfBanksEquity                        "+sumOfBanksEquity);
			System.out.println("");

			System.out.println("OFFICE FOR STATISTICS: LOAD AGENTS");
		}

		officeForStatistics.loadAgents();





		// trial cycle to be replaced with scheduled actions


		schedule = RunEnvironment.getInstance().getCurrentSchedule();



		if(verboseFlag){
			System.out.println("END OF INITIAL SETUP");
			System.out.println("==========================================");
		}


		if(schedulingFlag){
			officeForStatistics.scheduleEvents();
		}
		else{
			if(verboseFlag){
				System.out.println("START OF ITERATION CYCLE");
				System.out.println("OFFICE FOR STATISTICS: COMPUTE VARIABLES (PRODUCT DIFFUSION INDICATOR, PRODUCTIVITIES ...");
			}

			officeForStatistics.computeVariables();

			//		if(verboseFlag){
			System.out.println("FIRMS: MAKE PRODUCTION");
			//		}
			contextAction=contextActionFactory.createActionForIterable(firmsList,"makeProduction",false);
			contextAction.execute();

			officeForStatistics.performFirmsExit();


			if(verboseFlag){
				System.out.println("FIRMS: SET WAGE");
			}
			contextAction=contextActionFactory.createActionForIterable(firmsList,"setWorkersWage",false);
			contextAction.execute();

			//		if(verboseFlag){
			System.out.println("BANKS: UPDATE CONSUMERS ACCOUNTS (INTEREST AND ASK FOR REFUNDING)");
			//		}

			contextAction=contextActionFactory.createActionForIterable(banksList,"updateConsumersAccounts",false);
			contextAction.execute();

			if(verboseFlag){
				System.out.println("CONSUMERS: PAY BACK BANK DEBT");
			}
			contextAction=contextActionFactory.createActionForIterable(consumersList,"payBackBankDebt",false);
			contextAction.execute();

			//		if(verboseFlag){
			System.out.println("BANKS: RESET CONSUMERS DEMANDED AND ALLOWED CREDIT");
			//		}

			contextAction=contextActionFactory.createActionForIterable(banksList,"resetConsumersDemandedAndAllowedCredit",false);
			contextAction.execute();





			if(verboseFlag){
				System.out.println("STUDENTS: STEP STATE");
			}
			contextAction=contextActionFactory.createActionForIterable(consumersList,"stepStudentState",false);
			contextAction.execute();


			if(verboseFlag){
				System.out.println("CONSUMERS: STEP CONSUMPTION");
			}
			contextAction=contextActionFactory.createActionForIterable(consumersList,"stepConsumption",false);
			contextAction.execute();

			//		if(verboseFlag){
			System.out.println("BANKS: EXTEND CONSUMER CREDIT");
			//		}

			contextAction=contextActionFactory.createActionForIterable(banksList,"setAllowedConsumersCredit",false);
			contextAction.execute();


			if(verboseFlag){
				System.out.println("CONSUMERS: ADJUST CONSUMPTION ACCORDING TO EXTENDED CREDIT");
			}
			contextAction=contextActionFactory.createActionForIterable(consumersList,"adjustConsumptionAccordingToExtendedCredit",false);
			contextAction.execute();


			officeForStatistics.computeDesiredDemand();
			officeForStatistics.allocateDesiredDemand();

			if(verboseFlag){
				System.out.println("OFFICE FOR STATISTICS: MATCH DEMAND AND SUPPLY");
			}

			officeForStatistics.matchDemandAndSupply();

			officeForStatistics.computeDemand();
			officeForStatistics.allocateDemand();

			//			if(verboseFlag){
			System.out.println("CONSUMERS: UPDATE BANK ACCOUNT ACCORDING TO EFFECTIVE CONSUMPTION");
			//		}
			contextAction=contextActionFactory.createActionForIterable(consumersList,"updateBankAccountAccordingToEffectiveConsumption",false);
			contextAction.execute();



			//			if(verboseFlag){
			System.out.println("FIRMS COMPUTE ECONOMIC RESULT");
			//		}
			contextAction=contextActionFactory.createActionForIterable(firmsList,"computeEconomicResultAndCapitalDepreciation",false);
			contextAction.execute();

			//		if(verboseFlag){
			System.out.println("BANKS: UPDATE FIRMS ACCOUNTS (INTEREST AND ASK FOR REFUNDING)");
			//		}

			contextAction=contextActionFactory.createActionForIterable(banksList,"updateFirmsAccounts",false);
			contextAction.execute();


			//		if(verboseFlag){
			System.out.println("FIRMS: PAY BACK BANK DEBT");
			//		}

			contextAction=contextActionFactory.createActionForIterable(firmsList,"payBackBankDebt",false);
			contextAction.execute();

			//		if(verboseFlag){
			System.out.println("BANKS: RESET FIRMS DEMANDED AND ALLOWED CREDIT");
			//		}

			contextAction=contextActionFactory.createActionForIterable(banksList,"resetFirmsDemandedAndAllowedCredit",false);
			contextAction.execute();



			//if(verboseFlag){
			System.out.println("FIRMS COMPUTE DESIRED CREDIT");
			//		}
			contextAction=contextActionFactory.createActionForIterable(firmsList,"setDesiredCredit",false);
			contextAction.execute();

			//		if(verboseFlag){
			System.out.println("BANKS EXTEND FIRM CREDIT");
			//		}

			contextAction=contextActionFactory.createActionForIterable(banksList,"setAllowedFirmsCredit",false);
			contextAction.execute();

			//if(verboseFlag){
			System.out.println("FIRMS: ADJUST PRODUCTION CAPITAL AND BANK ACCOUNTS");
			//		}
			contextAction=contextActionFactory.createActionForIterable(firmsList,"adjustProductionCapitalAndBankAccount",false);
			contextAction.execute();

			//if(verboseFlag){
			System.out.println("OFFICE FOR STATISTICS: COMPUTE AGGREGATE INVESTMENTS AND SET INDUSTRIES' INVESTMENTS");
			//		}

			officeForStatistics.computeInvestments();

			if(Context.verboseFlag){
				System.out.println("OFFICE FOR STATISTICS: ALLOCATE INVESTMENTS ");
			}

			officeForStatistics.allocateInvestments();


			if(verboseFlag){
				System.out.println("JETTISONING CURRICULA");
			}
			contextAction=contextActionFactory.createActionForIterable(firmsList,"jettisoningCurricula",false);
			contextAction.execute();

			theLaborMarket.jettisoningCurricula();

			if(verboseFlag){
				System.out.println("FIRMS PERFORM DOWNWARD ADJUSTMENT OF LABOR FORCE");
			}
			contextAction=contextActionFactory.createActionForIterable(firmsList,"laborForceDownwardAdjustment",false);
			contextAction.execute();

			if(verboseFlag){
				System.out.println("CONSUMERS SEND CVs");
			}
			contextAction=contextActionFactory.createActionForIterable(consumersList,"sendJobApplications",false);
			contextAction.execute();
			if(verboseFlag){
				System.out.println("FIRMS: DIRECT HIRING");
			}
			contextAction=contextActionFactory.createActionForIterable(firmsList,"laborForceUpwardAdjustment",false);
			contextAction.execute();

			officeForStatistics.activateLaborMarket();

			/*
			   if(verboseFlag){
			   System.out.println("SET DESIRED CAPITAL");
			   }
			   contextAction=contextActionFactory.createActionForIterable(firmsList,"setDesiredProductionCapital",false);
			   contextAction.execute();
			   */

			officeForStatistics.performConsumersTurnover();
			officeForStatistics.performFirmsEntry();


		}
		if (RunEnvironment.getInstance().isBatch())
		{
			RunEnvironment.getInstance().endAt(batchStoppingTime);
		}


		return context;


			}



	}