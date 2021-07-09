import ast
import sys
import os
import re
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from distutils.util import strtobool
from matplotlib import cm
from matplotlib.colors import ListedColormap, LinearSegmentedColormap
from typing import List

""" Create heat maps summarising the satisfaction levels and population distributions for each agent type with and without social capital.
This is completed for each simulation version.

Parameters
---------
folderName : str
    The output destination folder, used to organise output data.
learningPercentages : str
    Array of the percentage of Agents that possibly used social Learning per day.
    Note that this is immediately converted to type List[int].
exchangesArray : str
    Array of the various number of exchanges per day that were simulated.
    Note that this is immediately converted to type List[int].
startingRatiosArray : str[]
    Array of the various agent type starting ratios that were simulated.
daysOfInterest : str
    Array containing the days to be analysed.
    Note that this is immediately converted to type List[int].
"""

folderName: str = sys.argv[1]
learningPercentages: List[int] = ast.literal_eval(sys.argv[2])
exchangesArray: List[int] = ast.literal_eval(sys.argv[3])

temp: str = re.sub('\[|\]| ', '', sys.argv[4])
startingRatiosArray: List[str] = list(temp.split(","))

daysOfInterest: List[int] = ast.literal_eval(sys.argv[5])

baseOutputDirectory: str = os.path.join(os.path.dirname(os.path.dirname(os.path.dirname(os.path.dirname(os.path.dirname(__file__))))), folderName)

simulationVersions: List[str] = []
for it in os.scandir(baseOutputDirectory):
    if it.is_dir():
        simulationVersions.append(it.path)

top1 = cm.get_cmap('Purples_r', 128)
bottom1 = cm.get_cmap('Greens', 128)

newcolors1 = np.vstack((top1(np.linspace(0, 1, 128)),
                       bottom1(np.linspace(0, 1, 128))))
newcmp1 = ListedColormap(newcolors1, name='GreenPurple')
     
for v in simulationVersions:
    if v.endswith('mixed'):
        socialCapital: bool = strtobool((v.split('useSC_')[1]).split('_')[0])

        for r in startingRatiosArray:

            # Create the output directories if they do not already exist.
            resultDir: str = v + "/comparative/images/heat_maps/" + r + "/"
            if not os.path.exists(resultDir):
                os.makedirs(resultDir)

            df_sat = pd.read_csv(v + "/comparative/data/heat_maps/" + r + "/mergedSummary.csv")
            df_pop = pd.read_csv(v + "/comparative/data/heat_maps/" + r + "/mergedPopulationSummary.csv")

            #rounds all value to 2 decimal places (for the sake of the heatmap visualisation
            df_sat = df_sat.round(2)
            df_pop = df_pop.round(2)

            selfish_subplots = []
            social_subplots = []
            population_summary = []
            for l in learningPercentages:
                df_sat_l = df_sat.loc[df_sat['%_Learning'] == l]
                df_pop_l = df_pop.loc[df_pop['%_Learning'] == l]

                selfish_pivot = df_sat_l.pivot("Day", "Exchanges", "Selfish")
                social_pivot = df_sat_l.pivot("Day", "Exchanges", "Social")
                population_pivot = df_pop_l.pivot("Day", "Exchanges", "%_Social")

                selfish_subplots.append(selfish_pivot)
                social_subplots.append(social_pivot)
                population_summary.append(population_pivot)

            columns = len(learningPercentages)
            fig, axs = plt.subplots(nrows=3, ncols=columns, sharex='col', sharey='row')

            for index, s in enumerate(selfish_subplots):
                #seaborn heatmap per pivot table
                sns.heatmap(s, cmap="Reds", center= 0.5, vmin=0, vmax=1.0, ax=axs[0][index], linewidths=0.1, linecolor="white", cbar=True, annot=True, annot_kws={"size": 8})
                title: str = "Learning " + str(learningPercentages[index]) + "%"
                axs[0][index].set_title(title, fontsize=6)
                axs[0][index].invert_yaxis()
                axs[0][index].set_ylabel('')
                axs[0][index].set_xlabel('')

            for index, s in enumerate(social_subplots):
                #seaborn heatmap per pivot table
                sns.heatmap(s, cmap="Reds", center= 0.5, vmin=0, vmax=1.0, ax=axs[1][index], linewidths=0.1, linecolor="white", cbar=True, annot=True, annot_kws={"size": 8})
                title: str = "Learning " + str(learningPercentages[index]) + "%"
                axs[1][index].set_title(title, fontsize=6)
                axs[1][index].invert_yaxis()
                axs[1][index].set_ylabel('')
                axs[1][index].set_xlabel('')
            
            for index, s in enumerate(population_summary):
                #seaborn heatmap per pivot table
                sns.heatmap(s, cmap=newcmp1, center= 50, vmin=0, vmax=100, ax=axs[2][index], linewidths=0.1, linecolor="white", cbar=True, annot=True, annot_kws={"size": 8})
                title: str = "Learning " + str(learningPercentages[index]) + "%"
                axs[2][index].set_title(title, fontsize=6)
                axs[2][index].invert_yaxis()
                axs[2][index].set_ylabel('')
                axs[2][index].set_xlabel('')

            #adjust position of subplot
            plt.subplots_adjust(hspace = .2)
            plt.subplots_adjust(wspace = .2)
            plt.subplots_adjust(top = .8)
            plt.subplots_adjust(bottom = .2)
            plt.subplots_adjust(left = .12)
            plt.subplots_adjust(right = .95)

            #set x and y axis labels and plot title
            fig.text(0.5, 0.02, 'Exchanges', ha='center', fontsize=14)
            fig.text(0.04, 0.5, 'Day', va='center', rotation='vertical', fontsize=14)

            title: str = "Social Capital Populations"
            if socialCapital:
                title = "With " + title
            else:
                title = "Without " + title

            fig.suptitle(title, fontsize=14)

            fname = resultDir + "/mergedSummary"

            plt.savefig(fname,
                dpi=None,
                facecolor='w',
                edgecolor='w',
                orientation='portrait',
                format=None,
                transparent=False,
                bbox_inches=None,
                pad_inches=0.1,
                metadata=None)