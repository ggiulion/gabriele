/*
GABRIELE: the General Agent Based Repast Implemented Extensible Laboratory for Economics
Copyright (C) 2018  Gianfranco Giulioni
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package gabriele.institutions;

import java.util.Date;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import repast.simphony.data2.AggregateDSCreator;
import repast.simphony.data2.AggregateDataSource;
import repast.simphony.util.collections.IndexedIterable;
import repast.simphony.engine.schedule.DefaultActionFactory;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.util.ContextUtils;
import repast.simphony.random.RandomHelper;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.engine.environment.RunState;

import gabriele.agents.Firm;
import gabriele.agents.Bank;
import gabriele.agents.Industry;
import gabriele.agents.Consumer;
import gabriele.institutions.LaborMarket;
import gabriele.institutions.Government;
import gabriele.institutions.CentralBank;
import gabriele.Context;
import gabriele.bargaining.AProductDemand;
import gabriele.utils.AbsoluteRankNonAggregateDataSource;

public class OfficeForStatistics{
	public repast.simphony.context.Context<Object> myContext;
	LaborMarket myLaborMarket;
	Government myGovernment;
	CentralBank myCentralBank;
	AggregateDataSource maximumAbsoluteRankDataSource,minimumAbsoluteRankDataSource;
	public static ArrayList<Industry> industriesList = new ArrayList<Industry>();

	public double maximumAbsoluteRank,minimumAbsoluteRank,aggregateProduction,totalWeightedProduction,aggregateDemand,aggregateInvestments,aggregateInvestmentsBeforeExchangingExistingCapital,aggregateUnusedProductionCapital,aggregateUnusedProductionCapitalForSale,aggregateLoans,aggregateDeposits,aggregateHouseholdDesiredChangeInCredit,aggregateHouseholdAllowedChangeInCredit,aggregateWage;
	public IndexedIterable firmsList,consumersList,banksList;

	Firm aFirm;
	Consumer aConsumer;
	Bank aBank;
	Object anObj;
	Industry anIndustry;
	Iterator<Industry> industriesListIterator;
	Iterator contextIterator;

	DefaultActionFactory statActionFactory;
	IAction statAction;
	ArrayList anOrderList;
	AProductDemand anOrder;
	ArrayList<Firm> newFirmsList;

	int[] numberOfWokersInADegree;
	double[] totalProductivityOfWorkersInADegree;
	public static double[] averageProductivityOfWorkersInADegree;
	int numberOfWorkers=0;
	int numberOfFirms,numberOfConsumers,numberOfStudents,numberOfUnemployed,numberOfFirmExits;
	double aggregatePromissoryNotes=0;
	int numberOfRetirements=0;
	double totalProductivity=0;
	public static double averageProductivity=0;

	ScheduleParameters scheduleParameters;
	public static FileWriter dataReadmeWriter,macroDataWriter,microDataWriterForConsumers,microDataWriterForFirms,microDataWriterForConsumersBankAccounts01,microDataWriterForConsumersBankAccounts02,microDataWriterForConsumersBankAccounts03,microDataWriterForConsumersBankAccounts04,microDataWriterForConsumersBankAccounts05,microDataWriterForConsumersBankAccounts06,microDataWriterForFirmsBankAccounts01,microDataWriterForFirmsBankAccounts02,microDataWriterForFirmsBankAccounts03,microDataWriterForFirmsBankAccounts04,microDataWriterForFirmsBankAccounts05,microDataWriterForFirmsBankAccounts06;
	public OfficeForStatistics(repast.simphony.context.Context<Object> con){
		myContext=con;

		AggregateDSCreator absoluteRankDScreator = new AggregateDSCreator(new AbsoluteRankNonAggregateDataSource());
		maximumAbsoluteRankDataSource= absoluteRankDScreator.createMaxSource("maximum absolute rank");
		minimumAbsoluteRankDataSource= absoluteRankDScreator.createMinSource("minimum absolute rank");

		statActionFactory = new DefaultActionFactory();

		int thisRunNumber=(int)RunState.getInstance().getRunInfo().getRunNumber();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'h'HHmmss");
		Date date = new Date();
		String timeStamp =dateFormat.format(date);
		//		String microDataOutputFileNamePis,microDataOutputFileNameId,microDataOutputFileNameAccruedInt,microDataOutputFileNameRefunding;
		String readmeDataOutputFileName,macroDataOutputFileName,microDataOutputFileNameForConsumers,microDataOutputFileNameForFirms,microDataOutputFileNameForConsumersBankAccounts01,microDataOutputFileNameForConsumersBankAccounts02,microDataOutputFileNameForConsumersBankAccounts03,microDataOutputFileNameForConsumersBankAccounts04,microDataOutputFileNameForConsumersBankAccounts05,microDataOutputFileNameForConsumersBankAccounts06,microDataOutputFileNameForFirmsBankAccounts01,microDataOutputFileNameForFirmsBankAccounts02,microDataOutputFileNameForFirmsBankAccounts03,microDataOutputFileNameForFirmsBankAccounts04,microDataOutputFileNameForFirmsBankAccounts05,microDataOutputFileNameForFirmsBankAccounts06;
		//		String parametersDataOutputFileName;
		readmeDataOutputFileName="zdata_aaa_readme.txt";
		if(Context.timeStampInFileName){
						microDataOutputFileNameForConsumers="zdata_micro_consumers_run_"+thisRunNumber+"_time_"+timeStamp+".csv";
						microDataOutputFileNameForFirms="zdata_micro_firms_run_"+thisRunNumber+"_time_"+timeStamp+".csv";
						microDataOutputFileNameForConsumersBankAccounts01="zdata_micro_consumersbankaccounts01_run_"+thisRunNumber+"_time_"+timeStamp+".csv";
						microDataOutputFileNameForConsumersBankAccounts02="zdata_micro_consumersbankaccounts02_run_"+thisRunNumber+"_time_"+timeStamp+".csv";
						microDataOutputFileNameForConsumersBankAccounts03="zdata_micro_consumersbankaccounts03_run_"+thisRunNumber+"_time_"+timeStamp+".csv";
						microDataOutputFileNameForConsumersBankAccounts04="zdata_micro_consumersbankaccounts04_run_"+thisRunNumber+"_time_"+timeStamp+".csv";
						microDataOutputFileNameForConsumersBankAccounts05="zdata_micro_consumersbankaccounts05_run_"+thisRunNumber+"_time_"+timeStamp+".csv";
						microDataOutputFileNameForConsumersBankAccounts06="zdata_micro_consumersbankaccounts06_run_"+thisRunNumber+"_time_"+timeStamp+".csv";
						microDataOutputFileNameForFirmsBankAccounts01="zdata_micro_firmsbankaccounts01_run_"+thisRunNumber+"_time_"+timeStamp+".csv";
						microDataOutputFileNameForFirmsBankAccounts02="zdata_micro_firmsbankaccounts02_run_"+thisRunNumber+"_time_"+timeStamp+".csv";
						microDataOutputFileNameForFirmsBankAccounts03="zdata_micro_firmsbankaccounts03_run_"+thisRunNumber+"_time_"+timeStamp+".csv";
						microDataOutputFileNameForFirmsBankAccounts04="zdata_micro_firmsbankaccounts04_run_"+thisRunNumber+"_time_"+timeStamp+".csv";
						microDataOutputFileNameForFirmsBankAccounts05="zdata_micro_firmsbankaccounts05_run_"+thisRunNumber+"_time_"+timeStamp+".csv";
						microDataOutputFileNameForFirmsBankAccounts06="zdata_micro_firmsbankaccounts06_run_"+thisRunNumber+"_time_"+timeStamp+".csv";
			//			microDataOutputFileNameId="zdata_micro_ids_run_"+thisRunNumber+"_time_"+timeStamp+".csv";
			//			microDataOutputFileNameAccruedInt="zdata_micro_accrued_run_"+thisRunNumber+"_time_"+timeStamp+".csv";
			//			microDataOutputFileNameRefunding="zdata_micro_refund_run_"+thisRunNumber+"_time_"+timeStamp+".csv";
			macroDataOutputFileName="zdata_macro_run_"+thisRunNumber+"_time_"+timeStamp+".csv";
			//			parametersDataOutputFileName="zdata_params_run_"+thisRunNumber+"_time_"+timeStamp+".csv";
		}
		else{
						microDataOutputFileNameForConsumers="zdata_micro_consumers_run_"+thisRunNumber+".csv";
						microDataOutputFileNameForFirms="zdata_micro_firms_run_"+thisRunNumber+".csv";
						microDataOutputFileNameForConsumersBankAccounts01="zdata_micro_consumersbankaccounts01_run_"+thisRunNumber+".csv";
						microDataOutputFileNameForConsumersBankAccounts02="zdata_micro_consumersbankaccounts02_run_"+thisRunNumber+".csv";
						microDataOutputFileNameForConsumersBankAccounts03="zdata_micro_consumersbankaccounts03_run_"+thisRunNumber+".csv";
						microDataOutputFileNameForConsumersBankAccounts04="zdata_micro_consumersbankaccounts04_run_"+thisRunNumber+".csv";
						microDataOutputFileNameForConsumersBankAccounts05="zdata_micro_consumersbankaccounts05_run_"+thisRunNumber+".csv";
						microDataOutputFileNameForConsumersBankAccounts06="zdata_micro_consumersbankaccounts06_run_"+thisRunNumber+".csv";
						microDataOutputFileNameForFirmsBankAccounts01="zdata_micro_firmsbankaccounts01_run_"+thisRunNumber+".csv";
						microDataOutputFileNameForFirmsBankAccounts02="zdata_micro_firmsbankaccounts02_run_"+thisRunNumber+".csv";
						microDataOutputFileNameForFirmsBankAccounts03="zdata_micro_firmsbankaccounts03_run_"+thisRunNumber+".csv";
						microDataOutputFileNameForFirmsBankAccounts04="zdata_micro_firmsbankaccounts04_run_"+thisRunNumber+".csv";
						microDataOutputFileNameForFirmsBankAccounts05="zdata_micro_firmsbankaccounts05_run_"+thisRunNumber+".csv";
						microDataOutputFileNameForFirmsBankAccounts06="zdata_micro_firmsbankaccounts06_run_"+thisRunNumber+".csv";
			//			microDataOutputFileNameId="zdata_micro_ids_run_"+thisRunNumber+".csv";
			//			microDataOutputFileNameAccruedInt="zdata_micro_accrued_run_"+thisRunNumber+".csv";
			//			microDataOutputFileNameRefunding="zdata_micro_refund_run_"+thisRunNumber+".csv";
			macroDataOutputFileName="zdata_macro_run_"+thisRunNumber+".csv";
			//			parametersDataOutputFileName="zdata_params_run_"+thisRunNumber+".csv";
		}

/*
AC Aggregate Comsumption
AI Aggregate Investment
AS Aggregate Supply
L Loans
D deposits
NF Number of Firms
NC Number of Consumers
NW Number of Worker
NS Number of Students
NR Number of Retirements
NFE Number of Firm Exits
LHd aggregateHouseholdDesiredChangeInCredit
LHs aggregateHouseholdAllowedChangeInCredit
*/


		if(Context.saveMacroData){

			try{
				dataReadmeWriter=new FileWriter(readmeDataOutputFileName);
				dataReadmeWriter.append("Contents of\n========================\nzdata_macro . . . file\n========================\n\nAC Aggregate Comsumption\nAOI Aggregate Orders of Investment (these goods will be produced in the following period)\nASH Aggregate Supply of products for consumption (Aggregate Supply is given by ASH plos AOI of the previous period)\nL Loans\nD deposits\nNF Number of Firms\nNC Number of Consumers\nNW Number of Worker\nNS Number of Students\nNR Number of Retirements\nNFE Number of Firm Exits\nLHd aggregateHouseholdDesiredChangeInCredit\nLHs aggregateHouseholdAllowedChangeInCredit\nAIB Aggegate investments before exchanging unused production capital\nUPCFS Unused production Capital For sale\n");
				dataReadmeWriter.flush();




				macroDataWriter=new FileWriter(macroDataOutputFileName);
				macroDataWriter.append("AC;AOI;ASH;L;D;NF;NC;NW;NS;NR;NFE;LHd;LHs;AIB;UPCFS\n");
				macroDataWriter.flush();

					dataReadmeWriter.append("\n\nContents of\n========================\nzdata_micro_consumers_run . . . file\n========================\n\nt time\nid identification number\nage age\nstudent true if student false if worker\nemployed true if employed, false for students and unemployed\nedu_successes numberOfSuccesfulPeriodsOfEducation\nedu_failures numberOfFailedPeriodsOfEducation\nproductivity consumers productivity\nec effective consumption\nwage wage\ndi1 disposable income when deciding desired consumption\ndi2 disposable income for consumption\ndc desired consumption\nec effective consumption");
				dataReadmeWriter.flush();
				   


				microDataWriterForConsumers=new FileWriter(microDataOutputFileNameForConsumers);
				microDataWriterForConsumers.append("t;id;age;student;employed;edu_successes;edu_failures;degree;productivity;wage;di1;di2;dc;ec\n");
				microDataWriterForConsumers.flush();


				dataReadmeWriter.append("\n\nContents of\n========================\nzdata_micro_firms_run . . . file\n========================\n\nt time\nid identification number\nage age\nne number of employee\nwpp workers potential production\ncpp capital potential production\nddc desired demand from consumers \ndc demand from consumers\ndi demand from firms\nsw sum of payed wages\npcbd production capital before depreciation\npcad production capital after depreciation\ncoher cash on hand when computing economic result\ncoharb cash on hand after refunding banks\nctaisdc credit to ask in setDesiredCreditMethod\nuaiba unpaid amounts in bank accounts\ndpc desired production capital\npc production capital");
				dataReadmeWriter.flush();

				microDataWriterForFirms=new FileWriter(microDataOutputFileNameForFirms);
				microDataWriterForFirms.append("t;id;age;ne;wpp;cpp;ddc;dc;di;sw;pcbd;pcad;coher;coharb;ctaisdc;uaiba;dpc;pc\n");
				microDataWriterForFirms.flush();



				dataReadmeWriter.append("\n\nContents of\n========================\nzdata_micro_consumersbankaccounts<n>_run . . . file\n========================\n\nThese files record info on each consumer bank accounts in the format\naccount 1 data|account 2 data|...\nwhere\naccount i data = tickCount,consumer id,bank id,account amount,demanded credit,allowed credit,unpaid amount\n\nThe various files give this information in different moment of the timetick:\n\nzdata_micro_consumersbankaccounts01  situation at the beginning of method Consumer.payBackBankDebt()\nzdata_micro_consumersbankaccounts02  situation at the end of method Consumer.payBackBankDebt()\nzdata_micro_consumersbankaccounts03  situation at the beginning of method Consumer.stepConsumption()\nzdata_micro_consumersbankaccounts04  situation at the end of method Consumer.stepConsumption()\nzdata_micro_consumersbankaccounts05  situation at the beginning of method Consumer.adjustConsumptionAccordingToExtendedCredit()\nzdata_micro_consumersbankaccounts06  situation at the end of method Consumer.updateBankAccountAccordingToEffectiveConsumption()");
				dataReadmeWriter.flush();

				microDataWriterForConsumersBankAccounts01=new FileWriter(microDataOutputFileNameForConsumersBankAccounts01);
				microDataWriterForConsumersBankAccounts02=new FileWriter(microDataOutputFileNameForConsumersBankAccounts02);
				microDataWriterForConsumersBankAccounts03=new FileWriter(microDataOutputFileNameForConsumersBankAccounts03);
				microDataWriterForConsumersBankAccounts04=new FileWriter(microDataOutputFileNameForConsumersBankAccounts04);
				microDataWriterForConsumersBankAccounts05=new FileWriter(microDataOutputFileNameForConsumersBankAccounts05);
				microDataWriterForConsumersBankAccounts06=new FileWriter(microDataOutputFileNameForConsumersBankAccounts06);


				dataReadmeWriter.append("\n\nContents of\n========================\nzdata_micro_firmsbankaccounts<n>_run . . . file\n========================\n\nThese files record info on each firm bank accounts in the format\naccount 1 data|account 2 data|...\nwhere\naccount i data = tickCount,firm id,bank id,account amount,demanded credit,allowed credit,unpaid amount\n\nThe various files give this information in different moment of the timetick:\n\nzdata_micro_firmsbankaccounts01  situation at the beginning of method Firm.payBackBankDebt()\nzdata_micro_firmsbankaccounts02  situation at the end of method Firm.payBackBankDebt()\nzdata_micro_firmsbankaccounts03  situation at the beginning of method Firm.stepDesiredCredit()\nzdata_micro_firmsbankaccounts04  situation at the end of method Firm.stepDesiredCredit()\nzdata_micro_firmsbankaccounts05  situation at the beginning of method Firm.adjustProductionCapitalAndBankAccount()\nzdata_micro_firmsbankaccounts06  situation at the end of method Firm.adjustProductionCapitalAndBankAccount()");
				dataReadmeWriter.flush();



				microDataWriterForFirmsBankAccounts01=new FileWriter(microDataOutputFileNameForFirmsBankAccounts01);
				microDataWriterForFirmsBankAccounts02=new FileWriter(microDataOutputFileNameForFirmsBankAccounts02);
				microDataWriterForFirmsBankAccounts03=new FileWriter(microDataOutputFileNameForFirmsBankAccounts03);
				microDataWriterForFirmsBankAccounts04=new FileWriter(microDataOutputFileNameForFirmsBankAccounts04);
				microDataWriterForFirmsBankAccounts05=new FileWriter(microDataOutputFileNameForFirmsBankAccounts05);
				microDataWriterForFirmsBankAccounts06=new FileWriter(microDataOutputFileNameForFirmsBankAccounts06);
				//				microDataWriterForConsumers.append("t;\n");
				//				microDataWriterForConsumers.flush();
			}
			catch(IOException e) {System.out.println("IOException");}
		}




	}

public void loadAgents(){
		try{
			firmsList=myContext.getObjects(Firm.class);
			consumersList=myContext.getObjects(Class.forName("gabriele.agents.Consumer"));
			banksList=myContext.getObjects(Class.forName("gabriele.agents.Bank"));
			myLaborMarket=(LaborMarket)(myContext.getObjects(Class.forName("gabriele.institutions.LaborMarket"))).get(0);
			myGovernment=(Government)(myContext.getObjects(Class.forName("gabriele.institutions.Government"))).get(0);
			myCentralBank=(CentralBank)(myContext.getObjects(Class.forName("gabriele.institutions.CentralBank"))).get(0);
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}

		if(Context.verboseFlag){
			System.out.println("     office for statistics: agents loaded");
		}
}

	public void computeVariables(){
		if(Context.verboseFlag){
/*
			System.out.println();
			System.out.println("===================================================================");
			System.out.println("SIMULATION TIME STEP: "+RepastEssentials.GetTickCount());
			System.out.println("====================================================================");
			System.out.println();
*/
			System.out.println("OFFICE FOR STATISTICS: COMPUTE VARIABLES (PRODUCT DIFFUSION INDICATOR, PRODUCTIVITIES ...");
		}
		/*
		//remove firms with 0 production

		ArrayList<Firm> firmsToRemove=new ArrayList<Firm>();
		Iterator contextIterator=myContext.iterator();
		while(contextIterator.hasNext()){
		anObj=contextIterator.next();
		if(anObj instanceof Firm){
		aFirm=(Firm)anObj;
		if(aFirm.getProduction()>0){
		}
		else{
		System.out.println("     firm "+aFirm.getIdentity()+" removed because producing "+aFirm.getProduction());
		firmsToRemove.add(aFirm);
		}
		}
		}

		for(int z=0;z<firmsToRemove.size();z++){
		myContext.remove(firmsToRemove.get(z));
		}
		*/
		try{
			firmsList=myContext.getObjects(Class.forName("gabriele.agents.Firm"));
			consumersList=myContext.getObjects(Class.forName("gabriele.agents.Consumer"));
			banksList=myContext.getObjects(Class.forName("gabriele.agents.Bank"));
			myLaborMarket=(LaborMarket)(myContext.getObjects(Class.forName("gabriele.institutions.LaborMarket"))).get(0);
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}
		numberOfFirms=firmsList.size();
		numberOfConsumers=consumersList.size();

		//Absolute Ranks
		maximumAbsoluteRankDataSource.reset();
		minimumAbsoluteRankDataSource.reset();
		industriesList = new ArrayList<Industry>();
		try{
			maximumAbsoluteRank=(double)maximumAbsoluteRankDataSource.get(myContext.getObjects(Class.forName("gabriele.agents.Firm")),0);
			minimumAbsoluteRank=(double)minimumAbsoluteRankDataSource.get(myContext.getObjects(Class.forName("gabriele.agents.Firm")),0);
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}
		if(Context.verboseFlag){
			System.out.println("     maximum absolute rank "+maximumAbsoluteRank+" minimum Absolute Rank "+minimumAbsoluteRank);
		}



		for(int i=0;i<(int)(maximumAbsoluteRank-minimumAbsoluteRank+1);i++){
			industriesList.add(new Industry((int)(i+minimumAbsoluteRank)));
		}

		//compute 1) aggregate production and demand 2) number of firms in each industry and 3) production and demand for each industry
		aggregateProduction=0;
		aggregateDemand=0;
		for(int i=0;i<firmsList.size();i++){
			aFirm=(Firm)firmsList.get(i);
			aggregateProduction+=aFirm.getProduction();
			aggregateDemand+=aFirm.getDemand();
			int position=(int)(aFirm.getProductAbsoluteRank()-minimumAbsoluteRank);
			anIndustry=industriesList.get(position);
			anIndustry.increaseNumberOfFirms();
			anIndustry.increaseProduction(aFirm.getProduction());
			anIndustry.increaseDemand(aFirm.getDemand());
			anIndustry.addFirm(aFirm);
		}
		//delete from the industries list those with zero production
		industriesListIterator=industriesList.iterator();
		while(industriesListIterator.hasNext()){
			anIndustry=industriesListIterator.next();
			if(anIndustry.getProduction()==0){
				industriesListIterator.remove();
			}
		}

		//print to terminal information on industries
		if(Context.verboseFlag){
			//			System.out.println("STATS OFFICE: NEW DATA FROM INDUSTRIES ARE AVAILABLE");
			industriesListIterator=industriesList.iterator();
			while(industriesListIterator.hasNext()){
				anIndustry=industriesListIterator.next();
				if(Context.verboseFlag){
					System.out.println("     absolute Rank "+anIndustry.getAbsoluteRank()+" number of firms "+anIndustry.getNumberOfFirms()+" production for howsehold "+anIndustry.getProduction()+" demand Of previous period + new entry expected demand "+anIndustry.getDemand());
				}
			}

			if(Context.verboseFlag){
				System.out.println("     aggregate production "+aggregateProduction+" aggregate demand of previous period "+aggregateDemand);
			}
		}

		//set relative rank for each firm

		int minimumAbsoluteRankOfFirmsWithPositiveProduction=industriesList.get(0).getAbsoluteRank();
		for(int i=0;i<industriesList.size();i++){
			anIndustry=industriesList.get(i);
			anIndustry.setRelativeRank(minimumAbsoluteRankOfFirmsWithPositiveProduction);
			anIndustry.setMarketShare(aggregateDemand);
		}

		//compute totalWeightedProduction
		totalWeightedProduction=0;
		for(int i=0;i<industriesList.size();i++){
			anIndustry=industriesList.get(i);
			totalWeightedProduction=totalWeightedProduction+anIndustry.getWeightedProduction();
		}
		//set product diffusion indicator for each industry
		for(int i=0;i<industriesList.size();i++){
			anIndustry=industriesList.get(i);
			anIndustry.setProductDiffusionIndicator(totalWeightedProduction);
		}
		//let firms computeAverageProductivityForEachDegreeOfEducation		
		statAction=statActionFactory.createActionForIterable(firmsList,"computeAverageProductivityForEachDegreeOfEducation",false);
		statAction.execute();
		// computeAverageProductivityForEachDegreeOfEducation for the economy		
		numberOfWokersInADegree=new int[7];
		totalProductivityOfWorkersInADegree=new double[7];
		averageProductivityOfWorkersInADegree=new double[7];
		numberOfWorkers=0;
		totalProductivity=0;

		for(int i=0;i<firmsList.size();i++){
			aFirm=(Firm)firmsList.get(i);
			int[] aFirmNumberOfWokersInADegree=aFirm.getnumberOfWokersInADegree();
			double[] aFirmTotalProductivityOfWorkersInADegree=aFirm.getTotalProductivityOfWorkersInADegree();
			for(int j=0;j<7;j++){
				numberOfWokersInADegree[j]=numberOfWokersInADegree[j]+aFirmNumberOfWokersInADegree[j];
				totalProductivityOfWorkersInADegree[j]=totalProductivityOfWorkersInADegree[j]+aFirmTotalProductivityOfWorkersInADegree[j];
				numberOfWorkers=numberOfWorkers+aFirmNumberOfWokersInADegree[j];
				totalProductivity=totalProductivity+aFirmTotalProductivityOfWorkersInADegree[j];


				if(numberOfWokersInADegree[j]>0){
					averageProductivityOfWorkersInADegree[j]=totalProductivityOfWorkersInADegree[j]/numberOfWokersInADegree[j];
				}
				else{
					averageProductivityOfWorkersInADegree[j]=0;
				}
			}

		}
		averageProductivity=totalProductivity/numberOfWorkers;

	// compute aggregate promissory notes
		aggregatePromissoryNotes=0;
		for(int i=0;i<firmsList.size();i++){
			aFirm=(Firm)firmsList.get(i);
			aggregatePromissoryNotes+=aFirm.getPromissoryNotes();
		}


		if(Context.verboseFlag){
			for(int z=0;z<7;z++){
				System.out.println("     system level: degree "+z+" workers "+numberOfWokersInADegree[z]+" total Productivity "+totalProductivityOfWorkersInADegree[z]+" average Prod "+averageProductivityOfWorkersInADegree[z]);
			}
			System.out.println("     system: number of workers "+numberOfWorkers+" total Productivity "+totalProductivity+" average productivity "+averageProductivity);
			System.out.println("     system: aggregate promissory notes "+aggregatePromissoryNotes);
		}
	//compute aggregate loans and Deposits	
		aggregateLoans=0;
		aggregateDeposits=0;
		for(int i=0;i<banksList.size();i++){
			aBank=(Bank)banksList.get(i);
			aBank.computeBalanceVariables();
			aggregateLoans+=aBank.getLoans();
			aggregateDeposits+=aBank.getDeposits();
		}
	//compute number of students and unemployed
		numberOfStudents=0;
		numberOfUnemployed=0;
		for(int i=0;i<consumersList.size();i++){
			aConsumer=(Consumer)consumersList.get(i);
			if(aConsumer.getIsStudentFlag()){
				numberOfStudents++;
			}
			else{
				if(!aConsumer.getIsWorkingFlag()){
					numberOfUnemployed++;
				}
			}
		}
	}

	public void implementFiscalPolicy(){
			if(Context.verboseFlag){
			System.out.println("OFFICE FOR STATISTICS, GOVERNMENT AND CENTRAL BANK: IMPLEMENT FISCAL POLICY");
			System.out.println("  OFFICE FOR STATISTICS: COMPUTE VARIABLES FOR FISCAL POLICY DECISION");
		}
		aggregateWage=0;
		for(int i=0;i<consumersList.size();i++){
			aConsumer=(Consumer)consumersList.get(i);
				if(aConsumer.getIsWorkingFlag()){
					aggregateWage+=aConsumer.getWage();
				}

		}
		if(Context.verboseFlag){
			System.out.println("     taxableIncome = aggregateWage "+aggregateWage+" numberOfUnemployed "+numberOfUnemployed);
		}

		if(Context.verboseFlag){
			System.out.println("  CENTRAL BANK: DECIDE ON GOVERNMENT FINANCING");
		}
		myCentralBank.setGovernmentAllowedCredit(aggregateProduction);
		if(Context.verboseFlag){
			System.out.println("  GOVERNMENT: DECIDE TAX RATE");
		}
		myGovernment.setTaxRate(numberOfUnemployed,aggregateWage);

		if(Context.verboseFlag){
			System.out.println("  CONSUMERS: ADJUST TO FISCAL POLICY");
		}
		statAction=statActionFactory.createActionForIterable(consumersList,"adjustToFiscalPolicy",false);
		statAction.execute();
	}

	public void resetProducAttractiveness(){
		try{
			firmsList=myContext.getObjects(Class.forName("gabriele.agents.Firm"));
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}


		//compute totalWeightedProduction
		totalWeightedProduction=0;
		for(int i=0;i<industriesList.size();i++){
			anIndustry=industriesList.get(i);
			anIndustry.setMarketShare(aggregateDemand);
			totalWeightedProduction=totalWeightedProduction+anIndustry.getWeightedProduction();
		}
		//set product diffusion indicator for each industry
		for(int i=0;i<industriesList.size();i++){
			anIndustry=industriesList.get(i);
			anIndustry.setProductDiffusionIndicator(totalWeightedProduction);
		}
	}

	/**
	 * The difference with the computeDemand method is that this method accounts for imperfection in the goods market 
	 */
	public void computeDesiredDemand(){
		aggregateDemand=0;
		statAction=statActionFactory.createActionForIterable(industriesList,"resetDemand",false);
		statAction.execute();


		try{
			consumersList=myContext.getObjects(Class.forName("gabriele.agents.Consumer"));
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}


		for(int i=0;i<consumersList.size();i++){
			aConsumer=(Consumer)consumersList.get(i);
			anOrderList=aConsumer.getOrders();

			for(int j=0;j<anOrderList.size();j++){
				anOrder=(AProductDemand)anOrderList.get(j);
				anIndustry=industriesList.get(j);
				double tmpDemand=anOrder.getDemand()*(1-Context.percentageOfDemandMissedBecauseOfGoodsMarketsInperfections);
				anIndustry.increaseDemand(tmpDemand);
				aggregateDemand=aggregateDemand+tmpDemand;
			}

		}

		if(Context.verboseFlag){
			System.out.println("OFFICE FOR STATISTICS: COMPUTE DESIRED DEMAND ");
		}
		industriesListIterator=industriesList.iterator();
		while(industriesListIterator.hasNext()){
			anIndustry=industriesListIterator.next();
			if(Context.verboseFlag){
				System.out.println("     absolute Rank "+anIndustry.getAbsoluteRank()+" number of firms "+anIndustry.getNumberOfFirms()+" production "+anIndustry.getProduction()+" demand from hosehold "+anIndustry.getDemand()+" demand from firms "+anIndustry.getInvestments());
			}
		}

		if(Context.verboseFlag){
			System.out.println("     Aggregate demand from howsehold "+aggregateDemand+" Aggregate demand from firms "+aggregateInvestments+" Aggregate production for household "+aggregateProduction);
		}
	}

	public void allocateDesiredDemand(){
		if(Context.verboseFlag){
			System.out.println("OFFICE FOR STATISTICS: ALLOCATE DESIRED DEMAND ");
		}
		statAction=statActionFactory.createActionForIterable(industriesList,"allocateDesiredDemand",false);
		statAction.execute();
	}

	public void computeDemand(){
		aggregateDemand=0;
		statAction=statActionFactory.createActionForIterable(industriesList,"resetDemand",false);
		statAction.execute();


		try{
			consumersList=myContext.getObjects(Class.forName("gabriele.agents.Consumer"));
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}


		for(int i=0;i<consumersList.size();i++){
			aConsumer=(Consumer)consumersList.get(i);
			anOrderList=aConsumer.getOrders();

			for(int j=0;j<anOrderList.size();j++){
				anOrder=(AProductDemand)anOrderList.get(j);
				anIndustry=industriesList.get(j);
				anIndustry.increaseDemand(anOrder.getDemand());
				aggregateDemand=aggregateDemand+anOrder.getDemand();
			}

		}

		if(Context.verboseFlag){
			System.out.println("OFFICE FOR STATISTICS: COMPUTE DEMAND ");
		}
		industriesListIterator=industriesList.iterator();
		while(industriesListIterator.hasNext()){
			anIndustry=industriesListIterator.next();
			if(Context.verboseFlag){
				System.out.println("     absolute Rank "+anIndustry.getAbsoluteRank()+" number of firms "+anIndustry.getNumberOfFirms()+" production "+anIndustry.getProduction()+" demand from hosehold "+anIndustry.getDemand()+" demand from firms "+anIndustry.getInvestments());
//				System.out.println("     absolute Rank "+anIndustry.getAbsoluteRank()+" number of firms "+anIndustry.getNumberOfFirms()+" production "+anIndustry.getProduction()+" demand "+anIndustry.getDemand());
			}
		}

		if(Context.verboseFlag){
			System.out.println("     Aggregate demand from howsehold "+aggregateDemand+" Aggregate demand from firms "+aggregateInvestments+" aggregate production for consumption "+aggregateProduction);
//			System.out.println("     Aggregate demand "+aggregateDemand+" aggregate production "+aggregateProduction);
		}



	}

	public void allocateDemand(){
		if(Context.verboseFlag){
			System.out.println("OFFICE FOR STATISTICS: ALLOCATE DEMAND ");
		}
		statAction=statActionFactory.createActionForIterable(industriesList,"allocateDemand",false);
		statAction.execute();
	}

	public void computeInvestments(){
		if(Context.verboseFlag){
			System.out.println("OFFICE FOR STATISTICS: COMPUTE AGGREGATE INVESTMENTS AND SET INDUSTRIES' INVESTMENTS");
		}
		aggregateInvestments=0;
		aggregateUnusedProductionCapital=0;
		//		statAction=statActionFactory.createActionForIterable(industriesList,"resetDemand",false);
		//		statAction.execute();


		try{
			firmsList=myContext.getObjects(Class.forName("gabriele.agents.Firm"));
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}


		for(int i=0;i<firmsList.size();i++){
			aFirm=(Firm)firmsList.get(i);
			double tmpInvestment=aFirm.getInvestment();
		if(Context.verboseFlag){
			System.out.println("     Firm "+aFirm.getIdentity()+" invest "+tmpInvestment);
		}
			if(tmpInvestment>=0){
				aggregateInvestments+=tmpInvestment;
			}
			else{
				aggregateUnusedProductionCapital+=-tmpInvestment;
			}
		}

		aggregateInvestmentsBeforeExchangingExistingCapital=aggregateInvestments;
		aggregateUnusedProductionCapitalForSale=aggregateUnusedProductionCapital*Context.percentageOfRealizedUnusedProductionCapital;

		if(aggregateInvestments>0 && aggregateUnusedProductionCapital>0){

			double shareOfUnusedCapitalSold;

			if(aggregateUnusedProductionCapitalForSale>aggregateInvestments){
				shareOfUnusedCapitalSold=aggregateInvestments/aggregateUnusedProductionCapitalForSale;
				aggregateInvestments=0;
			}
			else{
				shareOfUnusedCapitalSold=1;
				aggregateInvestments+=-aggregateUnusedProductionCapitalForSale;
			}

			for(int i=0;i<firmsList.size();i++){
				aFirm=(Firm)firmsList.get(i);
				double tmpInvestment=aFirm.getInvestment();
				if(tmpInvestment<0){
					aFirm.sellUnusedCapital(shareOfUnusedCapitalSold);
				}
			}
		}



		//		if(Context.verboseFlag){
		//			System.out.println("OFFICE FOR STATISTICS: COMPUTE INVESTMENTS ");
		//		}
		industriesListIterator=industriesList.iterator();
		while(industriesListIterator.hasNext()){
			anIndustry=industriesListIterator.next();
			anIndustry.setInvestments(anIndustry.getProductAttractiveness()*aggregateInvestments);
			if(Context.verboseFlag){
				System.out.println("     absolute Rank "+anIndustry.getAbsoluteRank()+" number of firms "+anIndustry.getNumberOfFirms()+" production "+anIndustry.getProduction()+" demand from household "+anIndustry.getDemand()+" demand from firms "+anIndustry.getInvestments());
			}
		}

		if(Context.verboseFlag){
			System.out.println("     Aggregate demand "+aggregateDemand+" aggregate production "+aggregateProduction+" aggregate investment before exchangin existing capital "+aggregateInvestmentsBeforeExchangingExistingCapital+" aggregate Investments "+aggregateInvestments+" unused capital "+aggregateUnusedProductionCapital+" unused capital for sale "+aggregateUnusedProductionCapitalForSale);
		}
	}

	public void allocateInvestments(){
		if(Context.verboseFlag){
			System.out.println("OFFICE FOR STATISTICS: ALLOCATE INVESTMENTS ");
		}
		statAction=statActionFactory.createActionForIterable(industriesList,"allocateInvestments",false);
		statAction.execute();
	}



	/**
	 * This method allocates the excess of demand of an industry to the industry that produces the next less advanced product. The cycle starts from the industry that produces the most advanced product.  
	 */
	public void matchDemandAndSupply(){
		if(Context.verboseFlag){
			System.out.println("OFFICE FOR STATISTICS: MATCH DEMAND AND SUPPLY");
		}
		double multiplier;
		double excessDemandToAllocate=0;
		for(int i=industriesList.size()-1;i>=0;i--){
			anIndustry=(Industry)industriesList.get(i);
			if((excessDemandToAllocate+anIndustry.getDemand())>anIndustry.getProduction()){
				multiplier=(anIndustry.getProduction()/anIndustry.getDemand());
				if(Context.verboseFlag){
					System.out.println("     absolute Rank "+anIndustry.getAbsoluteRank()+" number of firms "+anIndustry.getNumberOfFirms()+" production "+anIndustry.getProduction()+" demand "+anIndustry.getDemand()+" multiplier "+multiplier);
				}
				excessDemandToAllocate+= anIndustry.getDemand()-anIndustry.getProduction();
			}
			else{
				multiplier=((anIndustry.getDemand()+excessDemandToAllocate)/anIndustry.getDemand())*(1-Context.percentageOfDemandMissedBecauseOfGoodsMarketsInperfections);
				if(Context.verboseFlag){
					System.out.println("     absolute Rank "+anIndustry.getAbsoluteRank()+" number of firms "+anIndustry.getNumberOfFirms()+" production "+anIndustry.getProduction()+" demand "+anIndustry.getDemand()+" multiplier "+multiplier);
				}
				excessDemandToAllocate=0;
			}
			for(int j=0;j<consumersList.size();j++){
				aConsumer=(Consumer)consumersList.get(j);
				aConsumer.adjustConsumtionToMatchDemandAndSupply(i,multiplier);
			}
		}






		/*

		   if(aggregateDemand>aggregateProduction){
		   for(int i=0;i<industriesList.size();i++){
		   anIndustry=(Industry)industriesList.get(i);
		   multiplier=anIndustry.getProduction()/anIndustry.getDemand();
		   System.out.println("     absolute Rank "+anIndustry.getAbsoluteRank()+" number of firms "+anIndustry.getNumberOfFirms()+" production "+anIndustry.getProduction()+" demand "+anIndustry.getDemand()+" multiplier "+multiplier);
		   for(int j=0;j<consumersList.size();j++){
		   aConsumer=(Consumer)consumersList.get(j);
		   aConsumer.adjustConsumtionToMatchDemandAndSupply(i,multiplier);
		   }

		   }
		   }
		   else{
		   System.out.println("domanda insuff");
		   }
		   */
	}



	public void activateLaborMarket(){
		if(Context.verboseFlag){
			System.out.println("LABOR AGENCY");
		}
		try{
			myLaborMarket=(LaborMarket)(myContext.getObjects(Class.forName("gabriele.institutions.LaborMarket"))).get(0);
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}
		switch(Context.firmsWorkersMatching){
			case 0: if(Context.verboseFlag){
					System.out.println("     matching activity was not asked");
			}
			break;
			case 1:
			myLaborMarket.match();
			break;
			case 2:
			myLaborMarket.match();
			break;
			default: System.out.println("Unknown workers firms matching mechanism");
				 break;
		}
	}

	public void performConsumersTurnover(){
		if(Context.verboseFlag){
			System.out.println("CONSUMERS TURNOVER");
		}

		// identify firms to be removed
		ArrayList<Consumer> consumersToRemoveList = new ArrayList<Consumer>();
		ArrayList<Consumer> newConsumersList = new ArrayList<Consumer>();
		Consumer aNewConsumer;
		contextIterator=myContext.iterator();
		while(contextIterator.hasNext()){
			anObj=contextIterator.next();
			//check consumers
			if(anObj instanceof Consumer){
				aConsumer=(Consumer)anObj;
				if(aConsumer.getAge()>=Context.consumerExitAge){
//				if(aConsumer.getIsRetiredFlag()){
					aConsumer.computeWealth();
					if(Context.verboseFlag){
						System.out.println("     Exit  of consumer "+aConsumer.getIdentity()+" age "+aConsumer.getAge()+" wealth "+aConsumer.getWealth());
					}
					consumersToRemoveList.add(aConsumer);
					aNewConsumer=new Consumer(Context.consumersProgressiveIdentificationNumber,myContext,aConsumer.getBankAccountsList());
					contextIterator.remove();
					Context.consumersProgressiveIdentificationNumber++;
					newConsumersList.add(aNewConsumer);
				}
			}
		}
		numberOfRetirements=consumersToRemoveList.size();

		// remove
		for(int i=0;i<consumersToRemoveList.size();i++){
			myContext.remove(consumersToRemoveList.get(i));
		}

		// add new consumers to context
		for(int i=0;i<newConsumersList.size();i++){
			aConsumer=newConsumersList.get(i);
			if(Context.verboseFlag){
				System.out.println("     Entry of consumer "+aConsumer.getIdentity()+" age "+aConsumer.getAge()+" wealth "+aConsumer.getWealth());
			}
			aConsumer.changeBankAccountsOwner();
			myContext.add(aConsumer);
		}
	}

	public void performFirmsExit(){
		if(Context.verboseFlag){
			System.out.println("OFFICE FOR STATISTICS: FIRMS EXIT");
		}

		// identify firms to be removed
		ArrayList<Firm> exitingFirmsList = new ArrayList<Firm>();
		newFirmsList = new ArrayList<Firm>();
		Firm aNewFirm;
		contextIterator=myContext.iterator();
		while(contextIterator.hasNext()){
			anObj=contextIterator.next();
			//check firms
			if(anObj instanceof Firm){
				aFirm=(Firm)anObj;
				if((aFirm.getDemand()+aFirm.getOrdersOfProductsForInvestmentPurpose())<Context.thresholdDemandForFirmsExit){
//					System.out.println("   firm Exit "+(aFirm.getDemand()+aFirm.getOrdersOfProductsForInvestmentPurpose()));
					exitingFirmsList.add(aFirm);
					aFirm.setBankAccountsShutDown();
					aFirm.sendWorkersShutDownNew();
					aNewFirm=new Firm(Context.firmsProgressiveIdentificationNumber,myContext);
					Context.firmsProgressiveIdentificationNumber++;
					newFirmsList.add(aNewFirm);
				}

			}

		}
		numberOfFirmExits=exitingFirmsList.size();
		if(Context.verboseFlag){
System.out.println("     number of firms before exit "+firmsList.size());
		}
		// remove from context
		for(int i=0;i<exitingFirmsList.size();i++){
			aFirm=exitingFirmsList.get(i);
			if(Context.verboseFlag){
				System.out.println("     Exit of Firm "+aFirm.getID()+" demand from howsehold "+aFirm.getDemand()+" demand from Firms "+aFirm.getOrdersOfProductsForInvestmentPurpose()+" absolute Rank "+aFirm.getProductAbsoluteRank());
			}
			Industry tmpIndustry=aFirm.getIndustry();
			tmpIndustry.removeFirm(aFirm);
			tmpIndustry.decreaseNumberOfFirms();
			myContext.remove(aFirm);
		}





		try{
			firmsList=myContext.getObjects(Class.forName("gabriele.agents.Firm"));
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}
		if(Context.verboseFlag){
System.out.println("     number of firms after exit "+firmsList.size());
		}

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




	}

	public void banksRemoveExitedFirmsBankAccounts(){
		if(Context.verboseFlag){
			System.out.println("OFFICE FOR STATISTICS: MAKE BANK REMOVE ACCOUNTS OF EXITED FIRMS");
		}
		for(int i=0;i<banksList.size();i++){
			aBank=(Bank)banksList.get(i);
			aBank.removeExitedFirmsBankAccounts();
		}
	}

	public void performFirmsEntry(){
		if(Context.verboseFlag){
			System.out.println("OFFICE FOR STATISTICS: FIRMS ENTRY");
			System.out.println("     number of firms "+firmsList.size());
		}
		if(newFirmsList.size()>0){
/*
		try{
			consumersList=myContext.getObjects(Class.forName("gabriele.agents.Consumer"));
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}
*/

		for(int i=0;i<newFirmsList.size();i++){
			aFirm=newFirmsList.get(i);
			//			aFirm.setProductAbsoluteRank(RandomHelper.nextIntFromTo((int)minimumAbsoluteRank,(int)maximumAbsoluteRank));
			int tmpAbsoluteRank=(int)maximumAbsoluteRank;
			aFirm.setProductAbsoluteRank(tmpAbsoluteRank);
			aFirm.setupBankAccountOfNewEnteringFirm();
/*
			if(i<numberOfUnemployed){
				aFirm.setupBankAccount();
			}
			else{
				aFirm.setupBankAccountAtFullEmployement();
			}
*/
			for(int j=0;j<industriesList.size();j++){
				anIndustry=(Industry)industriesList.get(j);
				if(anIndustry.getAbsoluteRank()==tmpAbsoluteRank){
					anIndustry.addFirm(aFirm);
					anIndustry.increaseNumberOfFirms();
				}
			}
			myContext.add(aFirm);
			if(Context.verboseFlag){
				System.out.println("     Entry of Firm "+aFirm.getID()+" absolute Rank "+aFirm.getProductAbsoluteRank());
			}
		}
		resetProducAttractiveness();

	}

		try{
			firmsList=myContext.getObjects(Class.forName("gabriele.agents.Firm"));
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}


		if(Context.verboseFlag){
			System.out.println("     number of firms "+firmsList.size());
		}

	}

/*	
	public void performFirmsEntry(){
		if(Context.verboseFlag){
			System.out.println("OFFICE FOR STATISTICS: FIRMS ENTRY");
			System.out.println("     number of firms "+firmsList.size());
		}
		if(newFirmsList.size()>0){

		try{
			consumersList=myContext.getObjects(Class.forName("gabriele.agents.Consumer"));
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}
		int numberOfUnemployed=0;
		for(int j=0;j<consumersList.size();j++){
			aConsumer=(Consumer)consumersList.get(j);
			if(aConsumer.getIsWorkingFlag()){
			}
			else{
				if(!aConsumer.getIsStudentFlag()){
					numberOfUnemployed++;
				}
			}
		}



		for(int i=0;i<newFirmsList.size();i++){
			aFirm=newFirmsList.get(i);
			//			aFirm.setProductAbsoluteRank(RandomHelper.nextIntFromTo((int)minimumAbsoluteRank,(int)maximumAbsoluteRank));
			int tmpAbsoluteRank=(int)maximumAbsoluteRank;
			aFirm.setProductAbsoluteRank(tmpAbsoluteRank);
			if(i<numberOfUnemployed){
				aFirm.setupBankAccount();
			}
			else{
				aFirm.setupBankAccountAtFullEmployement();
			}
			for(int j=0;j<industriesList.size();j++){
				anIndustry=(Industry)industriesList.get(j);
				if(anIndustry.getAbsoluteRank()==tmpAbsoluteRank){
					anIndustry.addFirm(aFirm);
					anIndustry.increaseNumberOfFirms();
				}
			}
			myContext.add(aFirm);
			if(Context.verboseFlag){
				System.out.println("     Entry of Firm "+aFirm.getID()+" absolute Rank "+aFirm.getProductAbsoluteRank());
			}
		}
		resetProducAttractiveness();

	}

		try{
			firmsList=myContext.getObjects(Class.forName("gabriele.agents.Firm"));
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}


		if(Context.verboseFlag){
			System.out.println("     number of firms "+firmsList.size());
		}

	}
*/


	public void setupNewFirmsToComputeProductAttractiveness(){
		if(Context.verboseFlag){
			System.out.println("OFFICE FOR STATISTICS: SETUP NEW FIRMS VARIABLE TO COMPUTE PRODUCT ATTACTIVENESS");
		}
		if(newFirmsList.size()>0){
			for(int i=0;i<newFirmsList.size();i++){
				aFirm=newFirmsList.get(i);
				aFirm.setProductionAndDemandIfNewEntry();
			}
		}
	}



	public void publishIndustriesStats(){
		try{
			consumersList=myContext.getObjects(Class.forName("gabriele.agents.Consumer"));
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}


		//		statAction=statActionFactory.createActionForIterable(consumersList,"showInfoOnIndustries",false);
		statAction=statActionFactory.createActionForIterable(consumersList,"stepConsumption",false);
		statAction.execute();
	}

	public void scheduleEvents(){

		//		scheduleParameters=ScheduleParameters.createOneTime(120,51.0);
		//		Context.schedule.schedule(scheduleParameters,this,"introduceInnovation");

		//		scheduleParameters=ScheduleParameters.createOneTime(120,51.0);
		//		Context.schedule.schedule(scheduleParameters,this,"increaseUnemploymentDole");

		//		scheduleParameters=ScheduleParameters.createOneTime(120,51.0);
		//		Context.schedule.schedule(scheduleParameters,this,"increaseHouseholdDebtPropensityWithBankBehaviorUnchanged");

		//		scheduleParameters=ScheduleParameters.createOneTime(120,51.0);
		//		Context.schedule.schedule(scheduleParameters,this,"increaseHouseholdDebtPropensityWithRestrictiveBankBehavior");





		scheduleParameters=ScheduleParameters.createRepeating(1,1,50.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleFirmsMakeProduction");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,49.0);
		Context.schedule.schedule(scheduleParameters,this,"computeVariables");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,48.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleFirmsSetWage");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,47.5);
		Context.schedule.schedule(scheduleParameters,this,"implementFiscalPolicy");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,47.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleBanksUpdateConsumersAccounts");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,46.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleConsumersPayBackBankDebt");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,45.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleBanksResetConsumersDemandedAndAllowedCredit");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,44.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleStepStudentState");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,43.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleConsumersStepDesiredConsumption");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,42.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleBanksSetAllowedConsumersCredit");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,41.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleConsumersAdjustConsumptionAccordingToExtendedCredit");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,40.0);
		Context.schedule.schedule(scheduleParameters,this,"computeDesiredDemand");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,39.0);
		Context.schedule.schedule(scheduleParameters,this,"allocateDesiredDemand");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,38.0);
		Context.schedule.schedule(scheduleParameters,this,"matchDemandAndSupply");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,37.0);
		Context.schedule.schedule(scheduleParameters,this,"computeDemand");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,36.0);
		Context.schedule.schedule(scheduleParameters,this,"allocateDemand");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,35.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleConsumersUpdateBankAccountAccordingToEffectiveConsumption");
		if(Context.saveMicroData){
			scheduleParameters=ScheduleParameters.createRepeating(1,1,34.5);
			Context.schedule.schedule(scheduleParameters,this,"scheduleSaveConsumersData");
		}

		scheduleParameters=ScheduleParameters.createRepeating(1,1,34.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleFirmsComputeEconomicResultAndCapitalDepreciation");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,33.5);
		Context.schedule.schedule(scheduleParameters,this,"performFirmsExit");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,33.2);
		Context.schedule.schedule(scheduleParameters,this,"banksRemoveExitedFirmsBankAccounts");


		scheduleParameters=ScheduleParameters.createRepeating(1,1,33.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleBanksUpdateFirmsAccounts");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,32.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleFirmsPayBackBankDebt");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,31.5);
		Context.schedule.schedule(scheduleParameters,this,"scheduleFirmsStepProductInnovationProcess");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,31.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleBanksResetFirmsDemandedAndAllowedCredit");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,30.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleFirmsSetDesiredCredit");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,20.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleBanksSetAllowedFirmsCredit");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,19.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleFirmsSetPossibleInvestment");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,18.5);
		Context.schedule.schedule(scheduleParameters,this,"performFirmsEntry");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,16.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleFirmsJettisoningCurricula");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,15.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleOfficeForLaborJettisoningCurricula");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,14.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleFirmsLaborForceDownwardAdjustment");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,13.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleConsumersSendJobApplications");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,12.0);
		Context.schedule.schedule(scheduleParameters,this,"scheduleFirmsLaborForceUpwardAdjustment");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,11.0);
		Context.schedule.schedule(scheduleParameters,this,"activateLaborMarket");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,10.8);
		Context.schedule.schedule(scheduleParameters,this,"scheduleFirmsAdjustProductionCapitalAndBankAccount");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,10.5);
		Context.schedule.schedule(scheduleParameters,this,"computeInvestments");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,10.2);
		Context.schedule.schedule(scheduleParameters,this,"allocateInvestments");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,10.0);
		Context.schedule.schedule(scheduleParameters,this,"performConsumersTurnover");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,9.0);
		Context.schedule.schedule(scheduleParameters,this,"setupNewFirmsToComputeProductAttractiveness");

		if(Context.saveMicroData){
			scheduleParameters=ScheduleParameters.createRepeating(1,1,8.5);
			Context.schedule.schedule(scheduleParameters,this,"scheduleSaveFirmsData");
		}
		if(Context.saveMacroData){
			scheduleParameters=ScheduleParameters.createRepeating(1,1,8.0);
			Context.schedule.schedule(scheduleParameters,this,"scheduleSaveMacroData");
		}
		if(Context.verboseFlag){
			scheduleParameters=ScheduleParameters.createRepeating(1,1,7.5);
			Context.schedule.schedule(scheduleParameters,this,"scheduleEndOfSimulationStepMessage");
		}
	}

	public void scheduleFirmsMakeProduction(){
		if(Context.verboseFlag){
			System.out.println();
			System.out.println("===================================================================");
			System.out.println("SIMULATION TIME STEP: "+RepastEssentials.GetTickCount());
			System.out.println("====================================================================");
			System.out.println();
			System.out.println("FIRMS: MAKE PRODUCTION");
		}
		statAction=statActionFactory.createActionForIterable(firmsList,"makeProduction",false);
		statAction.execute();
	}

	public void scheduleFirmsSetWage(){
		if(Context.verboseFlag){
			System.out.println("FIRMS: SET WAGE");
		}
		statAction=statActionFactory.createActionForIterable(firmsList,"setWorkersWage",false);
		statAction.execute();
	}
	public void scheduleBanksUpdateConsumersAccounts(){
		if(Context.verboseFlag){
			System.out.println("BANKS: UPDATE CONSUMERS ACCOUNTS (INTEREST AND ASK FOR REFUNDING)");
		}
		statAction=statActionFactory.createActionForIterable(banksList,"updateConsumersAccounts",false);
		statAction.execute();
	}
	public void scheduleConsumersPayBackBankDebt(){
		if(Context.verboseFlag){
			System.out.println("CONSUMERS: PAY BACK BANK DEBT");
		}
		statAction=statActionFactory.createActionForIterable(consumersList,"payBackBankDebt",false);
		statAction.execute();
	}
	public void scheduleBanksResetConsumersDemandedAndAllowedCredit(){
		if(Context.verboseFlag){
			System.out.println("BANKS: RESET CONSUMERS DEMANDED AND ALLOWED CREDIT");
		}
		statAction=statActionFactory.createActionForIterable(banksList,"resetConsumersDemandedAndAllowedCredit",false);
		statAction.execute();
	}
	public void scheduleStepStudentState(){
		if(Context.verboseFlag){
			System.out.println("STUDENTS: STEP STATE");
		}
		statAction=statActionFactory.createActionForIterable(consumersList,"stepStudentState",false);
		statAction.execute();
	}
	public void scheduleConsumersStepDesiredConsumption(){
		if(Context.verboseFlag){
			System.out.println("CONSUMERS: STEP DESIRED CONSUMPTION");
		}
		statAction=statActionFactory.createActionForIterable(consumersList,"stepDesiredConsumption",false);
		statAction.execute();
	}
	public void scheduleBanksSetAllowedConsumersCredit(){
		if(Context.verboseFlag){
			System.out.println("BANKS: EXTEND CONSUMER CREDIT");
		}
		statAction=statActionFactory.createActionForIterable(banksList,"setAllowedConsumersCredit",false);
		statAction.execute();
		aggregateHouseholdDesiredChangeInCredit=0;
		aggregateHouseholdAllowedChangeInCredit=0;
		for(int i=0;i<banksList.size();i++){
			aBank=(Bank)banksList.get(i);
			aggregateHouseholdDesiredChangeInCredit+=aBank.getSumOfHouseholdDesiredChangeInCredit();
			aggregateHouseholdAllowedChangeInCredit+=aBank.getSumOfHouseholdAllowedChangeInCredit();
		}
		if(Context.verboseFlag){
			System.out.println("     aggregateHouseholdDesiredChangeInCredit "+aggregateHouseholdDesiredChangeInCredit);
			System.out.println("     aggregateHouseholdAllowedChangeInCredit "+aggregateHouseholdAllowedChangeInCredit);
		}
	}
	public void scheduleConsumersAdjustConsumptionAccordingToExtendedCredit(){
		if(Context.verboseFlag){
			System.out.println("CONSUMERS: ADJUST CONSUMPTION ACCORDING TO EXTENDED CREDIT");
		}
		statAction=statActionFactory.createActionForIterable(consumersList,"adjustConsumptionAccordingToExtendedCredit",false);
		statAction.execute();
	}
public void scheduleSaveConsumersData(){
		statAction=statActionFactory.createActionForIterable(consumersList,"saveDataToFile",false);
		statAction.execute();
}
public void scheduleSaveFirmsData(){
		statAction=statActionFactory.createActionForIterable(firmsList,"saveDataToFile",false);
		statAction.execute();
}

	public void scheduleConsumersUpdateBankAccountAccordingToEffectiveConsumption(){
		if(Context.verboseFlag){
			System.out.println("CONSUMERS: UPDATE BANK ACCOUNT ACCORDING TO EFFECTIVE CONSUMPTION");
		}
		statAction=statActionFactory.createActionForIterable(consumersList,"updateBankAccountAccordingToEffectiveConsumption",false);
		statAction.execute();
	}
	public void scheduleFirmsComputeEconomicResultAndCapitalDepreciation(){
		if(Context.verboseFlag){
			System.out.println("FIRMS: COMPUTE ECONOMIC RESULT");
		}
		statAction=statActionFactory.createActionForIterable(firmsList,"computeEconomicResultAndCapitalDepreciation",false);
		statAction.execute();
	}
	public void scheduleBanksUpdateFirmsAccounts(){
		if(Context.verboseFlag){
			System.out.println("BANKS: UPDATE FIRMS ACCOUNTS (INTEREST AND ASK FOR REFUNDING)");
		}
		statAction=statActionFactory.createActionForIterable(banksList,"updateFirmsAccounts",false);
		statAction.execute();
	}
	public void scheduleFirmsPayBackBankDebt(){
		if(Context.verboseFlag){
			System.out.println("FIRMS: PAY BACK BANK DEBT");
		}
		statAction=statActionFactory.createActionForIterable(firmsList,"payBackBankDebt",false);
		statAction.execute();
	}

	public void scheduleFirmsStepProductInnovationProcess(){
		if(Context.verboseFlag){
			System.out.println("FIRMS: STEP PRODUCT INNOVATION PROCESS");
		}
		statAction=statActionFactory.createActionForIterable(firmsList,"stepProductInnovationProcess",false);
		statAction.execute();
	}

	public void scheduleBanksResetFirmsDemandedAndAllowedCredit(){
		if(Context.verboseFlag){
			System.out.println("BANKS: RESET FIRMS DEMANDED AND ALLOWED CREDIT");
		}
		statAction=statActionFactory.createActionForIterable(banksList,"resetFirmsDemandedAndAllowedCredit",false);
		statAction.execute();
	}
	public void scheduleFirmsSetDesiredCredit(){
		if(Context.verboseFlag){
			System.out.println("FIRMS COMPUTE DESIRED CREDIT");
		}
		statAction=statActionFactory.createActionForIterable(firmsList,"setDesiredCredit",false);
		statAction.execute();
	}
	public void scheduleBanksSetAllowedFirmsCredit(){
		if(Context.verboseFlag){
			System.out.println("BANKS: EXTEND FIRM CREDIT");
		}
		statAction=statActionFactory.createActionForIterable(banksList,"setAllowedFirmsCredit",false);
		statAction.execute();
	}
	public void scheduleFirmsSetPossibleInvestment(){
		if(Context.verboseFlag){
			System.out.println("FIRMS: SET POSSIBLE INVESTMENT");
		}
		statAction=statActionFactory.createActionForIterable(firmsList,"setPossibleInvestment",false);
		statAction.execute();
	}
	public void scheduleFirmsAdjustProductionCapitalAndBankAccount(){
		if(Context.verboseFlag){
			System.out.println("FIRMS: ADJUST PRODUCTION CAPITAL AND BANK ACCOUNTS");
		}
		statAction=statActionFactory.createActionForIterable(firmsList,"adjustProductionCapitalAndBankAccount",false);
		statAction.execute();
	}
	public void scheduleFirmsJettisoningCurricula(){
		if(Context.verboseFlag){
			System.out.println("FIRMS: JETTISONING CURRICULA");
		}
		statAction=statActionFactory.createActionForIterable(firmsList,"jettisoningCurricula",false);
		statAction.execute();
	}
	public void scheduleOfficeForLaborJettisoningCurricula(){
		if(Context.verboseFlag){
			System.out.println("OFFICE FOR LABOR: JETTISONING CURRICULA");
		}
		myLaborMarket.jettisoningCurricula();
	}
	public void scheduleFirmsLaborForceDownwardAdjustment(){
		if(Context.verboseFlag){
			System.out.println("FIRMS: PERFORM DOWNWARD ADJUSTMENT OF LABOR FORCE");
		}
		statAction=statActionFactory.createActionForIterable(firmsList,"laborForceDownwardAdjustment",false);
		statAction.execute();
	}
	public void scheduleConsumersSendJobApplications(){
		if(Context.verboseFlag){
			System.out.println("CONSUMERS: SEND CVs");
		}
		statAction=statActionFactory.createActionForIterable(consumersList,"sendJobApplications",false);
		statAction.execute();
	}
	public void scheduleFirmsLaborForceUpwardAdjustment(){
		if(Context.verboseFlag){
			System.out.println("FIRMS: DIRECT HIRING");
		}
		statAction=statActionFactory.createActionForIterable(firmsList,"laborForceUpwardAdjustment",false);
		statAction.execute();
	}

	public void increaseHouseholdDebtPropensityWithBankBehaviorUnchanged(){
		Context.maxPreferenceParameter=2.0;
	}
	public void increaseHouseholdDebtPropensityWithRestrictiveBankBehavior(){
		Context.maxPreferenceParameter=2.0;
		Context.consumersProbabilityToGetFunded=0.0;
	}
	public void increaseUnemploymentDole(){
		Context.unemploymentDole=20;
		Context.costEdu=20;
	}
	public void introduceInnovation(){
		aFirm=(Firm)firmsList.get(0);
		aFirm.innovate();
	}

	public void scheduleSaveMacroData(){
	//compute aggregate loans and Deposits	
		aggregateLoans=0;
		aggregateDeposits=0;
		for(int i=0;i<banksList.size();i++){
			aBank=(Bank)banksList.get(i);
			aBank.computeBalanceVariables();
			aggregateLoans+=aBank.getLoans();
			aggregateDeposits+=aBank.getDeposits();
		}

			try{
				macroDataWriter.append(""+aggregateDemand+";"+aggregateInvestments+";"+aggregateProduction+";"+aggregateLoans+";"+aggregateDeposits+";"+numberOfFirms+";"+numberOfConsumers+";"+numberOfWorkers+";"+numberOfStudents+";"+numberOfRetirements+";"+numberOfFirmExits+";"+(-aggregateHouseholdDesiredChangeInCredit)+";"+(-aggregateHouseholdAllowedChangeInCredit)+";"+aggregateInvestmentsBeforeExchangingExistingCapital+";"+aggregateUnusedProductionCapitalForSale+"\n");
				macroDataWriter.flush();
			}
			catch(IOException e) {System.out.println("IOException");}
	}


	public void scheduleEndOfSimulationStepMessage(){
		System.out.println();
		System.out.println("===================================================================");
		System.out.println("END OF SIMULATION TIME STEP: "+RepastEssentials.GetTickCount());
		System.out.println("====================================================================");
		System.out.println();
	}

}
