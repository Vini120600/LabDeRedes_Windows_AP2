import React, { useState, useEffect } from 'react';
import { makeStyles } from '@material-ui/styles';
import useRouter from 'utils/useRouter';
import axios from 'utils/axios';
import { Page } from 'components';
import { LinearProgress, Snackbar, Typography} from '@material-ui/core';
import { Alert } from 'components';
import FileForm from './components/Form';
import FileUpLoad from 'views/FileUpLoad/FileUpLoad';

const useStyles = makeStyles(theme => ({
  root: {
    padding: theme.spacing(3)
  },
  results: {
    marginTop: theme.spacing(3)
  },
  alert: {
    marginBottom: theme.spacing(3)
  },
  formGroup: {
    marginBottom: theme.spacing(3)
  },
  fieldGroup: {
    display: 'flex',
    alignItems: 'center'
  },
  textField: {
    '& + &': {
      marginLeft: theme.spacing(2)
    }
  }
}));

const FileManagentDetails = () => {
  const classes = useStyles();

  const initialValues = {
    name: '',
    type: '',
    content: '',
    thumbnail: null,
    doc: null
  };

  const [file, setFile] = useState({ ...initialValues });
  const [isLoading, setLoading] = useState(false);
  const [isError, setIsError] = useState(false);
  const [error, setError] = useState({});
  const [openSnackbar, setOpenSnackbar] = useState(false);
  
  const router = useRouter();
  
  useEffect(() => {
    let mounted = true;
    
    if(!router.match.params.id) return;
    
    if (mounted) 
      fetchFile(router.match.params.id);

    return () => {
      mounted = false;
    };
  }, []);

  const fetchFile = (id) => {
    setLoading(true);
    axios.get('/api/v1/file/' + id).then(response => {
      setFile(response.data);
      setLoading(false);
      setIsError(false);
    }).catch((error) => {
      setLoading(false);
      setIsError(true);
      setError(error.response.data);
    });
  };

  const saveFile = (file) => {
    setLoading(true);
    file.doc = file.doc != undefined ? file.doc.key : null;
    axios({
      method: file.id ? 'PUT' : 'POST',
      url: '/api/v1/file' + (file.id ? `/${file.id}` : ''),
      data: file
    }).then(response => {
      setFile(response.data);
      setLoading(false);
      setIsError(false);
      setOpenSnackbar(true);
    }).catch((error) => {
      setLoading(false);
      setIsError(true);
      setError(error.response.data);
    });
  };

  const handleSnackbarClose = () => {
    setOpenSnackbar(false);
  };

  return (
    <Page
      className={classes.root}
      title="Lojas"
    >
      { (!isLoading && isError) && 
        <Alert
          variant="error"
          className={classes.alert}
          message={error.error}
        />
      }
      { isLoading && <LinearProgress /> }
      { (!isLoading && !isError) && 
        //<FileUpLoad file={file} onSubmit={saveFile}/>
        <FileForm file={file} onSubmit={saveFile} />
      }
      <Snackbar
        anchorOrigin={{
          vertical: 'bottom',
          horizontal: 'left'
        }}
        autoHideDuration={3000}
        message={
          <Typography
            color="inherit"
            variant="h6"
          >
            Dados salvo com sucesso.
          </Typography>
        }
        onClose={handleSnackbarClose}
        open={openSnackbar}
      />
    </Page>
  );
};

export default FileManagentDetails;
