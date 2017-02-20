FROM r-base

RUN R -e "install.packages('Rserve', repos = 'https://cloud.r-project.org')"

EXPOSE 6311

CMD [ "R", "-e", "Rserve::Rserve (debug = TRUE, args = '--no-save')" ]
